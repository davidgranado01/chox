package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DateHelper;


public class RepairBookedInDateOnSunday implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getBreBand().isRepairBookedInDate()){

            boolean success = true;

            if(claim.getHireMonitoringDetail() != null && claim.getHireMonitoringDetail().getRepairBookInDate()!=null){

                if(DateHelper.getDayOfWeek(claim.getHireMonitoringDetail().getRepairBookInDate())==1){
                    success = false;
                    narrative = "Repair was booked in on a Sunday.";
                }

            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

        }else{

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }

        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "039";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH; 
    }

}