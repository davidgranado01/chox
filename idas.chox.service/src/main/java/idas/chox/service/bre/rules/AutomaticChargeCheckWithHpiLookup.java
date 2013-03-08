package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class AutomaticChargeCheckWithHpiLookup implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        boolean success = true;

        if (claim.getBreBand().isAutomaticChargeCheckHpiLookup()
                && claim.getInvoice().getAutomaticFee().compareTo(BigDecimal.ZERO) != 0) {

            if (claim.getVehicleHire() == null || claim.getVehicleHire().getHpiVehicleTransmission() == null
                    || !claim.getVehicleHire().getHpiVehicleTransmission().toLowerCase().contains("auto")) {
                success = false;
                narrative = "The CHO is charging an automatic fee for hire and the HPI lookup did not identify the hire vehicle to be an automatic, please review need.";
            }
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

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
        return "063";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
