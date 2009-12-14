package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class RepairNetDoesNotExceedVehicleClassRepairNetCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Repair Net billed %s exceeds the Repair Net ceiling of %s for vehicle class %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isRepairNetDoesNotExceedVehicleClassRepairNetCeiling()) {

            BigDecimal repairNet = claim.getInvoice().getRepairNet();
            BigDecimal repairNetCeiling = claim.getBreBand().getMaxRepairNetCeiling();
            boolean success = repairNet.compareTo(repairNetCeiling) <= 0;

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (!success) {

                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(repairNet.doubleValue()),
                        moneyFormat.format(repairNetCeiling.doubleValue()),
                        claim.getCustomer().getVehicleClass().getCode());
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
        return "023";
    }

    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED;
    }
}
