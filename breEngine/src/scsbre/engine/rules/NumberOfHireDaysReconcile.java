package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class NumberOfHireDaysReconcile implements IBusinessRule {

    private String narrative = "";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if(claim.getBreBand().isNumberOfHireDaysReconcile()){
        
            boolean success = true;

            Integer dayDif = (CalcHelper.getDaysBetweenDates(claim.getHireDetail().getRentalStart(), claim.getHireDetail().getRentalEnd())+1);

            if(claim.getHireDetail().getDays() != dayDif){
                success =false;
                narrative = "The number of Hire Days billed does not reconcile with the Hire Start and Hire End dates provided";
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
        return "024";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceDataCalculationIncorrect;
    }

}
