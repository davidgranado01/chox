package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class ClaimRevert extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRevert.class);
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((securityInfoProvider.getIsCHO() && !claim.getStatus().equals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REJECTION_ACCEPTED) && !claim.getStatus().equals(ClaimStatus.INVOICE_REJECTED_ACCEPTED))
                || (securityInfoProvider.getIsINS() && !claim.getStatus().equals(ClaimStatus.CLAIM_REFERRED_TO_FNOL)
                && !claim.getStatus().equals(ClaimStatus.CLAIM_REF_TO_ENG) && !claim.getStatus().equals(ClaimStatus.INVOICE_REF_TO_ENG)
                && !claim.getStatus().equals(ClaimStatus.INVOICE_PAYMENT_LOGGED) && !claim.getStatus().equals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA))) {
            throw new AccessDeniedException("Not in correct role to revert claim in status '" + claim.getStatus() + "'.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("Reverting status for claim: {} (id={})", claim.getChoReference(), claim.getId());
        if (claimService.revertClaim(claim.getId()))
            LOG.info("Claim status reverted for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
        else
            LOG.warn("Failed to revert claim status for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        expectingStatuses.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
        expectingStatuses.add(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
    }
}