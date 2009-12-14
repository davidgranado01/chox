package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class HireNetDoesNotExceedVehicleClassHireNetCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Hire Net billed %s exceeds the Hire Net ceiling of %s for vehicle class %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isHireNetDoesNotExceedVehicleClassHireNetCeiling()) {

            BigDecimal hireNet = claim.getInvoice().getHireNet();

            BigDecimal hireNetCeiling = claim.getBreBand().getMaxHireNetCeiling();
            boolean success = hireNet.compareTo(hireNetCeiling) <= 0;

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (!success) {

                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(hireNet.doubleValue()),
                        moneyFormat.format(hireNetCeiling.doubleValue()),
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
        return "003";
    }

    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
