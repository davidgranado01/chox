package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.util.VehicleClassHelper;

public class HireNetDoesNotExceedVehicleClassHireNetCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Hire Net billed %s exceeds the Hire Net ceiling of %s for vehicle class %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isHireNetDoesNotExceedVehicleClassHireNetCeiling()) {

            BigDecimal hireNet = claim.getInvoice().getHireNet();

            BigDecimal hireNetCeiling = claim.getBreBand().getMaxHireNetCeiling();
            boolean success = hireNet.compareTo(hireNetCeiling) <= 0;

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (!success) {

                String cusVehicleClassName = "";
                if (VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {
                    cusVehicleClassName = claim.getCustomer().getVehicleClass().getName();
                }
                
                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(hireNet.doubleValue()),
                        moneyFormat.format(hireNetCeiling.doubleValue()),
                        cusVehicleClassName);
            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        return res;

    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "003";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
