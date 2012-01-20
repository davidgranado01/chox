package idas.chox.data.services;

import idas.chox.core.model.Attachment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.WebUserUserRoleService;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class AttachmentServiceImpl extends SecureDataService implements AttachmentService {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentServiceImpl.class);
    private WebUserUserRoleService webUserUserRoleService;

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    @Override
    public List getAttachmentsByClaim(int claimId) {

        StringBuilder sb = new StringBuilder();
        sb.append("select id, version, file_name, remarks, category, file_type, last_modified_date, claim_id, created_date from attachment ");
        sb.append("where claim_id=:pClaimId and deleted=false");
        Map extParameters = new HashMap();
        extParameters.put("pClaimId", claimId);
        return externalQuery(sb.toString(), extParameters);
    }

    @Override
    public Attachment getAttachment(int attachmentId) {
        return (Attachment) get(Attachment.class, attachmentId);
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
            save(attachment);
//            delete(attachment);
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
