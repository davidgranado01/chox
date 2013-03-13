package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class DualControlChargeCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            res.setIsVisibleToCHO(true);
        } else {
            res.setIsVisibleToCHO(false);
        }
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isDualControlChargeCheck()) {

            boolean success = true;

            if (claim.getInvoice().getDualControlFee().compareTo(BigDecimal.ZERO) > 0) {
                success = false;
                narrative = "The CHO is charging a dual control fee for the hire, please review need.";
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
        return "037";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isSubscriber(claimType)) {
            return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}

