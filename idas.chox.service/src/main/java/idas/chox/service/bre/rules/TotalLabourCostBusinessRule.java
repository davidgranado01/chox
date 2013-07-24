package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.CalcHelper;

public class TotalLabourCostBusinessRule implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(TotalLabourCostBusinessRule.class);
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'TotalLabourCostBusinessRule' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isTotalLabourCostBusinessRule() && claim.getInvoice().getRepairGross().compareTo(BigDecimal.ZERO) > 0) {


            LOG.debug(" 'TotalLabourCostBusinessRule' to claim {}. is active ", claim.getChoReference());

            boolean success = true;

            BigDecimal hundred = new BigDecimal("100.00");
            BigDecimal bLabourCost = null;
            BigDecimal brepairGross = claim.getInvoice().getRepairGross();
            BigDecimal vat_rate = CalcHelper.getVatRate(new Date());
            BigDecimal perRepairGross = brepairGross.multiply(hundred.subtract(vat_rate)).divide(hundred).setScale(2, BigDecimal.ROUND_HALF_UP);

            if (claim.getHireMonitoringDetail() != null && claim.getInvoice() != null) {
                bLabourCost = claim.getHireMonitoringDetail().getLabourCost();
            }

            LOG.debug("Percentage Repair Gross {} ", perRepairGross);
            LOG.debug("bLabourCost  {}",bLabourCost);

            if (bLabourCost != null && bLabourCost.compareTo(perRepairGross) >= 0) {
                success = false;
                narrative = "It seems that the CHO has supplied the Repair Gross as the value for the Total Labour Cost, please review.";

            } else {
                narrative = "";
                LOG.debug("TotalLabourCostBusinessRule passed.");
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        } else {
            LOG.debug("TotalLabourCostBusinessRule skipped.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        LOG.debug("TotalLabourCostBusinessRule End.");
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "062";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
