package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class AutomaticChargeCheck implements IBusinessRule {

    private String narrative = "";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        boolean success = true;
        
        if(claim.getBreBand().isAutomaticChargeCheck()){

            if(claim.getInvoice().getAutomaticFee().compareTo(new BigDecimal(0)) > 0){
                success = false;
                narrative = "The CHO is charging an automatic fee for the hire, please review need.";
            }
            
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
            
        }else{

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);
            
        }

        return res;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "028";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalatedToHandler;
    }

}
