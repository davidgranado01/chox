package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.util.List;

public class ClaimReopen extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(claim.getPreviousStatus());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
    }
}