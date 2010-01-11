package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.util.List;

public class ClaimRejectionAccept extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
    }
}