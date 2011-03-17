package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
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

        if (claim.getBreBand().isTotalLabourCostBusinessRule()) {


            LOG.debug(" 'TotalLabourCostBusinessRule' to claim {}. is active ", claim.getChoReference());

            boolean success = true;

            BigDecimal hundred = new BigDecimal(100);
            BigDecimal bLabourCost = BigDecimal.ZERO;
            BigDecimal brepairGross = BigDecimal.ZERO;
            BigDecimal vatRepairGross = BigDecimal.ZERO;
            BigDecimal perRepairGross = BigDecimal.ZERO;

            if (claim.getHireMonitoringDetail() != null && claim.getInvoice() != null) {
                bLabourCost = claim.getHireMonitoringDetail().getLabourCost();
                brepairGross = claim.getInvoice().getRepairGross();

            }
            BigDecimal vat_rate = CalcHelper.getVatRate(new Date());

            if (brepairGross != null) {
                vatRepairGross = brepairGross.multiply(hundred.subtract(vat_rate));
            }

            if (vatRepairGross != null) {
                perRepairGross = vatRepairGross.divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
            }

            LOG.debug(" vatRepairGross value  {}.  ", vatRepairGross);
            LOG.debug(" Percentage Repair Gross {} ", perRepairGross);
            LOG.debug("bLabourCost  {}",bLabourCost);

            if (bLabourCost.compareTo(perRepairGross) ==-1) {

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
    public String getStatusAfterFailure() {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
