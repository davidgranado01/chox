package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CHOBandCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class ActualHireDaysDoesNotExceedTotalLossInspection implements IBusinessRule {

    
    String narrative = "Number of hire days billed by the CHO exceeds the allowable days threshold for total loss hires.";
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getChoBand().isActualHireDaysDoesNotExceedTotalLossInspection()){

            if(claim.getHireDetail().getIsTotalLoss()){
                
                CHOBandCalcHelper bandCalc = CHOBandCalcHelper.getInstance(claim.getChoBand());
                boolean success =  claim.getHireDetail().getDays() <= bandCalc.getTotalLossInspectionDays();
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if(success) narrative = "";
                
            } else{
                res.setResult(RuleEvaluationResult.RuleSkipped);
                narrative = "Rule only applies when the clam is a total loss";
            }

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
        return "007";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }

}
