package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.*;


//rule 1, order 1


public class HasAllowedVehicleClass implements IBusinessRule {

    String narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        
        if(claim.getVClass() != null){
            boolean success = claim.getHireDetail().getVClass().getPrice().compareTo(claim.getVClass().getPrice()) <= 0;
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);   
            if(success) narrative = "";
        }
        else{
            narrative = "Customer vehicle class is not specified.";
            res.setResult(RuleEvaluationResult.RuleSkipped);
        }
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        return res;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "001";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }
}
