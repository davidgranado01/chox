package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.util.CHOBandCalcHelper;

public class ActualHireDaysDoesNotExceedTotalLossInspection implements IBusinessRule {

    String narrative = "Number of hire days billed by the CHO exceeds the allowable days threshold for total loss hires.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isActualHireDaysDoesNotExceedTotalLossInspection() && claim.getVehicleHire() != null) {

            if (claim.getVehicleHire().getIsTotalLoss()) {

                CHOBandCalcHelper bandCalc = CHOBandCalcHelper.getInstance(claim.getBreBand());
                boolean success = claim.getVehicleHire().getDays() <= bandCalc.getTotalLossInspectionDays();
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    narrative = "";
                }else{
//                    narrative = "Number of hire days billed by the CHO exceeds the allowable days threshold for total loss hires.";
                    narrative = "The number of hire days billed by the CHO (" + claim.getVehicleHire().getDays() +" days) exceeds the allowable days threshold (" + bandCalc.getTotalLossInspectionDays() + " days) for total loss hires.";
                }

            } else {
                res.setResult(RuleEvaluationResult.RuleSkipped);
                narrative = "Rule only applies when the clam is a total loss";
            }

        } else {

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
        return "007";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
