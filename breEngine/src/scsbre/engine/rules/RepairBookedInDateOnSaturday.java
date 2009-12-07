package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.DateHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class RepairBookedInDateOnSaturday implements IBusinessRule {

    private String narrative = "";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getBreBand().isRepairBookedInDate()){

            boolean success = true;

            if(claim.getHireMonitoringDetail().getRepairBookInDate()!=null){

                if(DateHelper.getDayOfWeek(claim.getHireMonitoringDetail().getRepairBookInDate())==7){
                    success = false;
                    narrative = "Repair was booked in on a Saturday.";
                }

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
        return "038";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalatedToHandler;
    }

}