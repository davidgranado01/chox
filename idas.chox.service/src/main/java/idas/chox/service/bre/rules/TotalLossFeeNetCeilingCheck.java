package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author John
 */
public class TotalLossFeeNetCeilingCheck implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(TotalLossFeeNetCeilingCheck.class);

    private String narrative;

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isTotalLossFeeNetCeilingCheck()) {
            Invoice invoice = claim.getInvoice();
            BigDecimal totalLossFeeNetCeiling = claim.getBreBand().getMaxAllowedTotalLossNetFee();
            boolean success = totalLossFeeNetCeiling.compareTo(invoice.getTotalLossFeeNet()) >= 0;
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
LOG.info("\nceiling={}, value={} - success={}\n", new Object[]{totalLossFeeNetCeiling.toString(), invoice.getTotalLossFeeNet(), success});
            if (success) {
                narrative = "";
            } else {
                narrative = "The CHO is charging more than the allowed value of £" + totalLossFeeNetCeiling.toString() + " for the Total Loss Fee. Please review.";
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
        return "091";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
