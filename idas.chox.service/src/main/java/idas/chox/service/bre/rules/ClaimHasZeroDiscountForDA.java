package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.CalcHelper;

/**
 *
 * @author Derm rule 17, order 10
 */
public class ClaimHasZeroDiscountForDA implements IBusinessRule {

    String narrative = "CHO is on DA scheme. Discount should be 0.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (claim.getBreBand().isClaimHasZeroDiscountForDA()) {

            if (!ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getChorganisation().isDelegatedAuthority()) {

                boolean success = CalcHelper.EqualTo(claim.getInvoice().getDiscount(), BigDecimal.ZERO);
                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

                if (success) {
                    narrative = "";
                }else{
                    narrative = "CHO is on DA scheme. Discount should be 0.";
                }

            } else {

                narrative = "Rule does not apply to CHOs not in the DA scheme";
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);

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
        return "017";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
