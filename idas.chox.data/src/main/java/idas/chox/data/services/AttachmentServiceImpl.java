package idas.chox.data.services;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Attachment;
import idas.chox.core.model.AttachmentFile;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Task;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.FileHelper;
import idas.chox.data.events.ChoxEvent;

public class AttachmentServiceImpl extends SecureDataService implements AttachmentService {

    private static final Logger LOG = LoggerFactory.getLogger(AttachmentServiceImpl.class);
    private WebUserUserRoleService webUserUserRoleService;
    protected ClaimService claimService;
    protected TaskService taskService;
    protected UserService userService;
    private EventService eventService;

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public List<Attachment> getAttachmentsByClaim(int claimId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Attachment.class);
        criteria.add(Restrictions.eq("deleted", false));
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.desc("id"));
        List<Attachment> attachmentList = findByCriteria(criteria);
        return attachmentList;
    }

    @Override
    public Attachment getAttachment(int attachmentId) {
        return (Attachment) get(Attachment.class, attachmentId);
    }

    private Claim getClaim(int claimId) {
        return (Claim) get(Claim.class, claimId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public boolean deleteAtatchment(int webUserId, int attachmentId) {
        LOG.debug("Deleting attachment with id={} for User with id={}", webUserId, attachmentId);
        WebUser webUser = null;
        if (webUserId > 0) {
            webUser = (WebUser) get(WebUser.class, webUserId);
            if (webUser == null) {
                LOG.error("No such user found with id={}", webUserId);
                throw new IllegalArgumentException("No such user.");
            }
        } else {
            LOG.error("No such user found with id={}", webUserId);
            throw new IllegalArgumentException("No such user.");
        }
        boolean canDelete = false;
        Attachment attachment = getAttachment(attachmentId);
        if (attachment == null) {
            LOG.error("No such attachment found with id={}", attachmentId);
            throw new IllegalArgumentException("No such attachment");
        }

        // Check Permission to delete attachment:
        //     Manager can delete from organisation,
        //     CHOX Admin can delete all
        //     owners can delete
        if (attachment.getCreatedBy().getId().intValue() == webUserId || userInRole(webUser, WebUserRole.ROLE_CHOX_ADMIN)) {
            canDelete = true;
        } else if (userInRole(webUser, WebUserRole.ROLE_CHO_MNG) && attachment.getCreatedBy().getChorganisation() != null
                && attachment.getCreatedBy().getChorganisation().getId().intValue() == webUser.getChorganisation().getId().intValue()) {
            canDelete = true;
        } else if (userInRole(webUser, WebUserRole.ROLE_INS_MNG) && attachment.getCreatedBy().getInsurer() != null
                && attachment.getCreatedBy().getInsurer().getId().intValue() == webUser.getInsurer().getId().intValue()) {
            canDelete = true;
        }

        LOG.debug("canDelete: {}", canDelete);
        if (canDelete) {
            attachment.setDeleted(true);

            Claim claim = attachment.getClaim();
            claim.setNoAttachments(claim.getNoAttachments() - 1);
            save(attachment);
            save(claim);
        }

        return canDelete;
    }

    @Override
    public boolean addAttachment(Claim claim, InputStream streamIn, String filename, long length, String category, String remark, boolean notify, boolean isInsurer, String whoCreated) {
        boolean result = false;

        byte fileContent[];
        try {
            fileContent = new byte[safeLongToInt(length)];
            streamIn.read(fileContent);
            streamIn.close();
            result = addAttachment(claim, fileContent, filename, length, category, remark, notify, isInsurer, whoCreated);
        } catch (Exception ex) {
            LOG.error("Error processing file with length={}: ", length, ex.getMessage(), ex);
        }

        return result;
    }

    @Override
    public boolean addAttachment(Claim claim, byte[] fileContent, String filename, long length, String category, String remark, boolean notify, boolean isInsurer, String whoCreated) {

        boolean bFlag = false;

        LOG.debug("Can read file '{}' of length {}", filename, length);
        String oldFileName = filename;
        String fileType = FileHelper.getFileExtension(oldFileName);
        String newFileName = FileHelper.getNewFileName(oldFileName, false);
        LOG.debug("Processing file {} of type {}", oldFileName, fileType);
        try {
            saveAttachement(claim, category, newFileName, remark, fileType, fileContent);
            bFlag = true;
            LOG.debug("Attachment saved.");
            if (notify) {
                Task task = new Task();
                task.setComplete(Boolean.FALSE);
                task.setDescription("The " + whoCreated + " has uploaded the following attachment '" + category + "' which requires review.");
                task.setDueDate(DateHelper.getCurrentDateTime());
                task.setType("Attachment");
                task.setVisibility(3);
                task.setRaisedBy(userService.findByUserName("system"));
                task.setInsurer(isInsurer);
                task.setClaim(claim);
                try {
                    taskService.createNewTask(task);
                } catch (Exception ex) {
                    LOG.warn("Failed to create an attachment notify task on claim '{}", claim.getChoReference());
                }
            }
        } catch (Exception ex) {
            LOG.error("Error processing file with length={}: ", length, ex);
        }

        return bFlag;
    }

    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    private void saveAttachement(
            Claim claim,
            String strCategory,
            String strFileName,
            String strRemark,
            String strFileType,
            byte[] obj) throws IOException {

        Attachment attachment = new Attachment();
        attachment.setFileName(strFileName);
        attachment.setRemarks(strRemark);
        attachment.setCategory(strCategory);
        attachment.setFileType(strFileType);
        claim.addAttachment(attachment);
        claim.setNoAttachments(claim.getNoAttachments() + 1);
        LOG.debug("Saving claim for the 1st time...");
        claimService.updateClaim(claim);
        AttachmentFile aFile = new AttachmentFile();
        aFile.setFileBuffer(obj);
        aFile.setAttachment(attachment);
        attachment.setAttachment(aFile);
        LOG.debug("Saving claim for the 2nd time...");
        claimService.updateClaim(claim);
        eventService.generate(claim, ChoxEvent.ATTACHMENT_UPLOADED_EVENT, attachment);
    }

    private boolean userInRole(WebUser user, String roleName) {

        List<WebUserUserRole> webUserUserRoles = webUserUserRoleService.getMappedUserRole(user.getId());

        for (WebUserUserRole webUserUserRole : webUserUserRoles) {
            if (webUserUserRole.getWebUserRole().getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
