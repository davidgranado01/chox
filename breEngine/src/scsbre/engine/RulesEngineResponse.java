package scsbre.engine;

import java.util.*;

import scsbre.model.ClaimStatus;

public class RulesEngineResponse {

    private List<RuleEvaluation> results = new ArrayList<RuleEvaluation>();
    private ClaimStatus status = null;

    public void addRuleEvaulation(RuleEvaluation res) {
        results.add(res);
    }

    public List<RuleEvaluation> getResults() {
        return results;
    }

    public ClaimStatus getStatus() {
        if (status == null) {
            
            boolean foundInvoiceDataCalculationIncorrect = false;
            boolean foundFailedRule = false;
            
            for (int i = 0; i < results.size(); i++) {
                RuleEvaluation rev = results.get(i);
                if (rev.getResult() == RuleEvaluationResult.RuleFailed) {
                    foundFailedRule = true;
                    if (rev.getRelatedRule().getStatusAfterFailure() 
                            == ClaimStatus.InvoiceDataCalculationIncorrect) {
                        foundInvoiceDataCalculationIncorrect = true;
                    }         
                }
            }
            
            if (foundFailedRule) {
                if(foundInvoiceDataCalculationIncorrect){
                    status = ClaimStatus.InvoiceDataCalculationIncorrect;
                }
                else {
                    status =  ClaimStatus.InvoiceEscalated;
                }
            }
            else{
                status = ClaimStatus.InvoiceApprovedByBRE;
            }
        }
        return status;
    }
}
