package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;
import idas.chox.service.bre.util.ClaimCalcHelper;
import java.math.BigDecimal;

public class HasCalculatedCorrectDailyRate implements IBusinessRule {

    String narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isHasCalculatedCorrectDailyRate()) {

            VehicleClass vehicleClass = claim.getCustomer().getVehicleClass();
            if (vehicleClass != null) {

                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

                // Mantis id: 630
                // Change to read vehicleHire's Vehicle Class
                // IVehicleClassInfo customerVClass = claim.getVClass();
                BigDecimal allowedDailyRate = new BigDecimal(0.00);
                allowedDailyRate = vehicleClass.getPrice().add(claim.getBreBand().getHireRateChargeTolerance());

                // LESS THAN OR EQUAL TO THE TRUE
                // boolean success = cCalc.getDailyHireRateChargedWithToleranceDeduction().compareTo(customerVClass.getPrice()) <= 0;
                boolean success = cCalc.getDailyHireRateCharged().compareTo(allowedDailyRate) <= 0;

                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    narrative = "";
                }

            } else {

                narrative = "Vehicle Hire vehicle class is not specified.";
                res.setResult(RuleEvaluationResult.RuleSkipped);
            }

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
        return "002";
    }

    public String getStatusAfterFailure() {
        // CARLSON @ 20091012
        // HasCalculatedCorrectDailyRate().applyToClaim(claim)) STATUS = InvoiceEscalatedToHandler;
        // return ClaimStatus.INVOICE_ESCALATED;
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
