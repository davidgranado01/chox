package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.ClaimService;
import java.util.List;

public class ClaimRevert extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRevert.class);
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
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
    }
}