package idas.chox.core.bre;

import idas.chox.core.model.ClaimStatus;
import java.util.*;

public class RulesEngineResponse {

    private List<RuleEvaluation> results = new ArrayList<RuleEvaluation>();
    private String status = null;

    public void addRuleEvaulation(RuleEvaluation res) {
        results.add(res);
    }

    public List<RuleEvaluation> getResults() {
        return results;
    }

    public String getStatus(boolean isEngineersEnabled) {
        if (status == null) {

            boolean foundInvoiceDataCalculationIncorrect = false;
            boolean foundInvoiceInvoiceEscalated = false;
            boolean foundFailedRule = false;

            for (int i = 0; i < results.size(); i++) {
                RuleEvaluation rev = results.get(i);

                if (rev.getResult() == RuleEvaluationResult.RuleFailed) {
                    foundFailedRule = true;

                    if (rev.getRelatedRule().getStatusAfterFailure() == ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT) {
                        foundInvoiceDataCalculationIncorrect = true;
                    }

                    if (rev.getRelatedRule().getStatusAfterFailure() == ClaimStatus.INVOICE_ESCALATED) {
                        foundInvoiceInvoiceEscalated = true;
                    }
                }
            }

            if (foundFailedRule) {

                if (foundInvoiceDataCalculationIncorrect) {

                    status = ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;

                } else {

                    status = ClaimStatus.INVOICE_ESCALATED_TO_CH;

                    if (foundInvoiceInvoiceEscalated && isEngineersEnabled) {
                        status = ClaimStatus.INVOICE_ESCALATED;
                    }

                }

            } else {
                status = ClaimStatus.INVOICE_APPROVED_BY_BRE;
            }
        }
        
        return status;
    }
}
