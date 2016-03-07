package idas.chox.service.bre.rules;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class EngineerNetFeeCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(EngineerNetFeeCheck.class);
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("In rule 'EngineerNetFeeCheck' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isEngineerNetFeeCheck()) {

            LOG.debug("Rule 'EngineerNetFeeCheck' active - applying to claim {}.", claim.getChoReference());
            if (claim.getInvoice().getEngineerFeeNet().compareTo(claim.getBreBand().getMaxAllowedEngineerNetFee()) > 0) {
                res.setResult(RuleEvaluationResult.RULE_FAILED);
                narrative = "The CHO is charging more than the allowed value of £" + claim.getBreBand().getMaxAllowedEngineerNetFee().setScale(2) + " for the Engineers Fee. Please review.";
            } else {
                res.setResult(RuleEvaluationResult.RULE_PASSED);
                narrative = "";
                LOG.debug("'EngineerNetFeeCheck' Business Rule Passed.");
            }

        } else {
            LOG.debug("'EngineerNetFeeCheck' Business Rule Skipped (not switched-on).");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);
        }

        LOG.debug("'EngineerNetFeeCheck' Business Rule Finished.");
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "090";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
