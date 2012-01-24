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
import idas.chox.service.bre.util.CalcHelper;
import java.util.Date;

public class TotalLabourCostBusinessRule implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(TotalLabourCostBusinessRule.class);
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'TotalLabourCostBusinessRule' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (!ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isTotalLabourCostBusinessRule() && claim.getInvoice().getRepairGross().compareTo(BigDecimal.ZERO) > 0) {


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

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

        } else {
            LOG.debug("TotalLabourCostBusinessRule skipped.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

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
    public String getStatusAfterFailure(boolean isTpiClaim) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
