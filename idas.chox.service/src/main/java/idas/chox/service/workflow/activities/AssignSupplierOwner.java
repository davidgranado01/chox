package idas.chox.service.workflow.activities;

import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.security.SecurityInfoProvider;

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
//        super.validate(claim); - this validates the claim status added in setupExpectingStatuses() below - not needed
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

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")) {
            throw new AccessDeniedException("Not in correct role to assign owner.");
        }
        LOG.debug("AssignSupplierOwner activity validated ok.");
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Assign supplier owner ('{}) to claim {}.", supplierClaimOwner.getFullName(), claim.getChoReference());
        claim.setSupplierClaimOwner(supplierClaimOwner);
        if (supplierClaimOwner.getTelephone() != null && supplierClaimOwner.getTelephone().length() > 0) {
            Comment comment = Comment.New(0, "Supplier Claims Handler is '" + supplierClaimOwner.getFullName() + "' (contact number: " + supplierClaimOwner.getTelephone() +")");
            claim.addComment(comment);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
/* - not needed as we don't need to validate statuses for this action
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        // For 'liability status update' & 'penalty charges to be applied' queues
        // we have to accept all statuses
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
 */
    }

}
