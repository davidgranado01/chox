package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.ClaimService;
import java.util.List;

public class SwitchClaim extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(SwitchClaim.class);
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("Switching claim status for claim: {} (id={})", claim.getChoReference(), claim.getId());
        if (claimService.switchClaim(claim.getId())) {
            LOG.info("Claim Switched for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
        } else {
            LOG.warn("Failed to Switch claim  for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);


    }
}
