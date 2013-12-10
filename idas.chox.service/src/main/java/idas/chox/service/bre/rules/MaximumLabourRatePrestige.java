package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class MaximumLabourRatePrestige implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(MaximumLabourRatePrestige.class);
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'MaximumLabourRate' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isMaximumLabourRatePrestigeCheck()
                && claim.getHireMonitoringDetail() != null
                && claim.getHireMonitoringDetail().getLabourRate() != null) {


            LOG.debug("Applying 'MaximumLabourRate' Business Rule to claim {}.", claim.getChoReference());

            boolean success = true;

            BigDecimal maxLabourRate = claim.getBreBand().getMaxAllowedLabourRate();
            BigDecimal labourRate = claim.getHireMonitoringDetail().getLabourRate();

 
            if (labourRate.compareTo(maxLabourRate) > 0) {
                success = false;
                narrative = "The CHO are charging £" + labourRate.toString()
                        + " per hour for labour and the allowed rate per hour is £"
                        + maxLabourRate + ".";

            } else {
                narrative = "";
                LOG.debug("'MaximumLabourRate' Business Rule Passed.");
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        } else {
            LOG.debug("'MaximumLabourRate' Business Rule Skipped.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        LOG.debug("'MaximumLabourRate' Business Rule Finished.");
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "088";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
