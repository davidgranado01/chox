package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.util.List;

public class ClaimRejectionConstest extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) {
        if (claim.getPreviousStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)
                || claim.getPreviousStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
            claim.setWorkgroup(null);
        }
        else
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), claim.getReasonOfRejection(), null);

        if (chainActivity != null) {
            chainActivity.setWorkflowContext(processContext);
            chainActivity.processInBatch(claim);
        }
    }


    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
    }
}
