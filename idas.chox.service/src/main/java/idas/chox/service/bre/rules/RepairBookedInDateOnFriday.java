package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DateHelper;

public class RepairBookedInDateOnFriday implements IBusinessRule {

    private String narrative = "";

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isRepairBookedInDate()) {

            boolean success = true;

            if (claim.getHireMonitoringDetail().getRepairBookInDate() != null) {

                if (DateHelper.getDayOfWeek(claim.getHireMonitoringDetail().getRepairBookInDate()) == 6) {
                    success = false;
                    narrative = "Repair was booked in on a Friday.";
                }

            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }

        return res;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "026";
    }

    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
