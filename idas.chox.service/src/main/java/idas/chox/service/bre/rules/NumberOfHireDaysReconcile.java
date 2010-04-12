package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.util.CalcHelper;

public class NumberOfHireDaysReconcile implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(NumberOfHireDaysReconcile.class);
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if (claim.getBreBand().isNumberOfHireDaysReconcile()) {

            boolean success = true;

            Integer dayDif = (CalcHelper.getDaysBetweenDates(claim.getVehicleHire().getRentalStart(), claim.getVehicleHire().getRentalEnd()) + 1);

            if (claim.getVehicleHire().getDays() != dayDif) {
                LOG.info("Rule failed: {} != {}", claim.getVehicleHire().getDays(), dayDif);
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

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "024";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
