package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class VedChargeCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isVedChargeCheck()) {

            boolean success = true;
            BigDecimal vedDailyRate = claim.getInvoice().getVedFee();
            if (claim.getInvoice().getVedQty() != null && claim.getInvoice().getVedQty() > 0) {
                vedDailyRate = vedDailyRate.divide(new BigDecimal(claim.getInvoice().getVedQty()));
            }
            
            if (vedDailyRate.compareTo(claim.getBreBand().getVedChargeCeiling()) > 0) {
                success = false;
                narrative = "The daily VED Charge of £" + vedDailyRate.toPlainString() + " is greater than the VED Charge Ceiling of £" + claim.getBreBand().getVedChargeCeiling() + ".";
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
        return "098";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
