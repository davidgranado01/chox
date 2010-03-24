package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.History;
import java.util.List;

public class InvoiceRejectionConstest extends BaseActivity {

    @Override
    protected void doProcess(Claim claim) throws Exception {

        /* RESUBMIT INVOICE FEOM CHO SHOULD PERFORM BRE VALIDATION AGAIN */
        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        for (History history : History.New(response)) {
            claim.addHistory(history);
        }

        if ((response.getStatus()).equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
        	claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
            throw new Exception("ERROR : Invoice data calculation incorrect");
        } else {
        	claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
    }
}