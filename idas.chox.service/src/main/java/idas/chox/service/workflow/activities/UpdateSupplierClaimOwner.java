package idas.chox.service.workflow.activities;

import org.springframework.security.access.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;

/**
 *
 * @author john
 */
public class UpdateSupplierClaimOwner extends BaseActivity {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private static final Logger LOG = LoggerFactory.getLogger(UpdateSupplierClaimOwner.class);
    private int supplierClaimOwnerId = 0;
    private WebUser newClaimOwner;
    private String oldClaimOwner;
    private UserService userService;
    // </editor-fold>
    
    public int getSupplierClaimOwnerId() {
        return supplierClaimOwnerId;
    }

    public void setSupplierClaimOwnerId(int supplierClaimOwnerId) {
        this.supplierClaimOwnerId = supplierClaimOwnerId;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getOldClaimOwner() {
        return oldClaimOwner;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (supplierClaimOwnerId > 0) {
            try {
                newClaimOwner = userService.getWebUser(supplierClaimOwnerId);
                // Check user belongs to the CHO
                if (newClaimOwner.getChorganisation().getId().intValue() != claim.getChorganisation().getId()) {
                    throw new AccessDeniedException("The selected Claim Owner does not belong to the CHO of the claim.");
                }
                // Check user belongs to the CHO
                if (newClaimOwner.getChorganisation().getId() != claim.getChorganisation().getId().intValue()) {
                    throw new AccessDeniedException("The selected Claim Owner does not belong to the CHO of the claim.");
                }
                // Check owner is different from current owner
                if (claim.getSupplierClaimOwner() != null && claim.getSupplierClaimOwner().getId().intValue() == newClaimOwner.getId().intValue()) {
                    throw new AccessDeniedException("No change to Supplier Claim Owner - not updating.");
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown getting web user with id={}: {}", supplierClaimOwnerId, ex.getMessage(), ex);
                throw new AccessDeniedException("An internal error occurred - please try again. If this problem persists, please contact CHOX Support.");
            }
        } else {
            throw new AccessDeniedException("No Supplier Claim Owner provided.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        Comment comment;
        WebUser oldClaimOwnerUser = claim.getSupplierClaimOwner();
        
        // SET COMMENT
        if (oldClaimOwnerUser != null) {
            oldClaimOwner = oldClaimOwnerUser.getFullName();

            if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
                comment = Comment.newComment(0, "Supplier Claim Owner changed from '" + oldClaimOwner
                        + "' to '" + newClaimOwner.getFullName()
                        + "' (contact number: " + newClaimOwner.getTelephone() + ")", true);
            } else {
                comment = Comment.newComment(0, "Supplier Claim Owner changed from '" + oldClaimOwner
                        + "' to '" + newClaimOwner.getFullName() + "'", true);
            }
        } else if (newClaimOwner.getTelephone() != null && newClaimOwner.getTelephone().length() > 0) {
            comment = Comment.newComment(0, "Supplier Claim Owner is '" + newClaimOwner.getFullName()
                    + "' (contact number: " + newClaimOwner.getTelephone() + ")", true);
        } else {
            comment = Comment.newComment(0, "Supplier Claim Owner is '" + newClaimOwner.getFullName() + "'", true);
        }
        claim.addComment(comment);
        claim.setSupplierClaimOwner(newClaimOwner);
    }
}
