package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Attachment;
import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.WebUserUserRoleService;

public class AttachmentServiceImpl extends SecureDataService implements AttachmentService {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentServiceImpl.class);
    private WebUserUserRoleService webUserUserRoleService;

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
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

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteAtatchment(int webUserId, int attachmentId){
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
        }
        else if (userInRole(webUser, WebUserRole.ROLE_CH_MNG) && attachment.getCreatedBy().getChorganisation() != null
                && attachment.getCreatedBy().getChorganisation().getId().intValue() == webUser.getChorganisation().getId().intValue()) {
            canDelete = true;
        }
        else if (userInRole(webUser, WebUserRole.ROLE_INS_MNG) && attachment.getCreatedBy().getInsurer() != null
                && attachment.getCreatedBy().getInsurer().getId().intValue() == webUser.getInsurer().getId().intValue()) {
            canDelete = true;
        }
        
        LOG.debug("canDelete: {}", canDelete);
        if (canDelete) {
            attachment.setDeleted(true);
            
            Claim claim = attachment.getClaim();
            claim.setNoAttachments(claim.getNoAttachments() -1);
            save(attachment);
            save(claim);
        }
        
        return canDelete;
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
