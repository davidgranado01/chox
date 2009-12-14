package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.util.CalcHelper;

public class NumberOfHireDaysReconcile implements IBusinessRule {

    private String narrative = "";

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if (claim.getBreBand().isNumberOfHireDaysReconcile()) {

            boolean success = true;

            Integer dayDif = (CalcHelper.getDaysBetweenDates(claim.getVehicleHire().getRentalStart(), claim.getVehicleHire().getRentalEnd()) + 1);

            if (claim.getVehicleHire().getDays() != dayDif) {
                success = false;
                narrative = "The number of Hire Days billed does not reconcile with the Hire Start and Hire End dates provided";
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
        return "024";
    }

    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
