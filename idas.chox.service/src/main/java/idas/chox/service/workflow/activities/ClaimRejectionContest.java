package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

public class ClaimRejectionContest extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) {
        if (claim.getPreviousStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        }
        else if (claim.getPreviousStatus().equals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED)) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        }
        else {
            claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), claim.getReasonOfRejection(), null);
        activityEventGenerator.generate(claim, this);

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

}
