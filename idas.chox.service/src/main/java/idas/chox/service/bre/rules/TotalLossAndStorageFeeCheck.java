package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Invoice;

/**
 *
 * @author John
 */
public class TotalLossAndStorageFeeCheck implements IBusinessRule {

    private String narrative;

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isTotalLossAndStorageFeeCheck()) {
            Invoice invoice = claim.getInvoice();
            boolean success = invoice.getTotalLossFeeNet().compareTo(BigDecimal.ZERO) > 0 && invoice.getStorageRecoveryNet().compareTo(BigDecimal.ZERO) > 0;
            res.setResult(!success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (success) {
                narrative = "";
            } else {
                narrative = "The CHO is charging a Total Loss Fee and the CHO are also charging for Storage & Recovery. Please review.";
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
        return "092";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
