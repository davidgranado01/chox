package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.util.List;

public class NewClaim extends BaseActivity {

    @Override
    protected void beforeProcess(Claim claim) {
        if (claim.getHireMonitoringDetail() != null && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {

            claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
        }
        //Normalize caim number
        String claimNumber = claim.getClaimNumber();
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claim.setClaimNumber(claimNumber.trim());
        }
    }

    @Override
    protected void validate(Claim claim) throws Exception {

        if (!claim.isTransient()) {
            throw new Exception("An process new claim attempt failed due to claim is already exist.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
    }

    @Override
    protected String getCurrentStatus() {
        return "";
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(null);
    }

}
