package scsbre.engine;

import java.util.*;

import scsbre.model.ClaimStatus;

public class RulesEngineResponse {

    private List<RuleEvaluation> results = new ArrayList<RuleEvaluation>();

    public void addRuleEvaulation(RuleEvaluation res) {
        results.add(res);
    }
    
    public List<RuleEvaluation> getResults(){
        
        return results;
    }

    public ClaimStatus getStatus() {
        
        
        boolean foundAwaitingPaymentPack = false;
        boolean foundFailedRule = false;
        for(int i = 0; i < results.size(); i++)
        {
            
            RuleEvaluation rev = results.get(i);
                
            if(rev.getResult() == RuleEvaluationResult.RuleFailed){
                
                foundFailedRule = true;
                foundAwaitingPaymentPack = rev.getRelatedRule().getStatusAfterFailure() == ClaimStatus.AwaitingPaymentPack;
                
                
            }
        }
        
        if(foundFailedRule){
            
            if(foundAwaitingPaymentPack){
                return ClaimStatus.AwaitingPaymentPack;
            }
            else return ClaimStatus.InvoiceEscalated;
        }
        
        return ClaimStatus.InvoiceApproved;
        
        
    }
}
