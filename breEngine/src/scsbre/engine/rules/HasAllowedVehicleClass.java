package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.*;


//rule 1, order 1


public class HasAllowedVehicleClass implements IBusinessRule {

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        
        if(claim.getVClass() != null){
            boolean success = claim.getHireDetail().getVClass().getPrice().compareTo(claim.getVClass().getPrice()) <= 0;
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);    
        }
        else{
            res.setResult(RuleEvaluationResult.RuleSkipped);
        }
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        return res;
    }

    public String getFailureMessage() {
        return "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";
    }

    public String getRuleId() {
        return "001";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.AwaitingPaymentPack;
    }
}
