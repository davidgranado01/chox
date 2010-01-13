package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.History;
import java.util.List;

public class NewInvoice extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) throws Exception {
        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);

        for (History history : History.New(response)) {
            claim.addHistory(history);
        }

        claim.setStatus(response.getStatus());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }
}
