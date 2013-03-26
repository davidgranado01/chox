package idas.chox.service.workflow.activities;


import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;

/**
 *
 * @author John
 */
public class AssignSupplierOwner extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(AssignSupplierOwner.class);
    private int supplierClaimOwnerId;
    private WebUser supplierClaimOwner;

    public int getSupplierClaimOwnerId() {
        return supplierClaimOwnerId;
    }

    public void setSupplierClaimOwnerId(int supplierClaimOwnerId) {
        this.supplierClaimOwnerId = supplierClaimOwnerId;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim); 
        LOG.debug("Validating AssignSupplierOwner activity");

        if (supplierClaimOwnerId <= 0) {
            throw new Exception("Invalid user id. supplierClaimOwnerId : "+supplierClaimOwnerId);
        } else {
            supplierClaimOwner = (WebUser) getDataService().get(WebUser.class, supplierClaimOwnerId);
            if (supplierClaimOwner == null) {
                throw new Exception("Invalid user id. supplierClaimOwner is null");
            }
            // Check user belongs to the CHO
            if (supplierClaimOwner.getChorganisation().getId().intValue() != claim.getChorganisation().getId().intValue()) {
                throw new AccessDeniedException("The selected Claim Owner does not belong to the CHO of the claim.");
            }
        }

        LOG.debug("AssignSupplierOwner activity validated ok.");
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Assign supplier owner ('{}) to claim {}.", supplierClaimOwner.getFullName(), claim.getChoReference());
        claim.setSupplierClaimOwner(supplierClaimOwner);
        if (supplierClaimOwner.getTelephone() != null && supplierClaimOwner.getTelephone().length() > 0) {
            Comment comment = Comment.newComment(0, "Supplier Claims Handler is '" + supplierClaimOwner.getFullName() + "' (contact number: " + supplierClaimOwner.getTelephone() +")");
            claim.addComment(comment);
        }
    }

}
