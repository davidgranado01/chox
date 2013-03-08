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
import idas.chox.service.bre.util.ClaimCalcHelper;

public class LabourCostBusinessRule implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(LabourCostBusinessRule.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'LabourCostBusinessRule' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isLabourCostBusinessRule() && claim.getVehicleHire() != null) {

            boolean success = true;
            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

            if (isRequiredToValidateByBRE(claim)) {

                int iNumberOfHireDay = claim.getVehicleHire().getDays();
                int iNumberDayOfLabourCostWorthy = cCalc.getNumberDayOfLabourCostWorthy();

                if (iNumberOfHireDay > iNumberDayOfLabourCostWorthy) {
                    success = false;
                    narrative = "The number of hire days billed by the CHO (" + iNumberOfHireDay + " days) is not relative to the number of expected hire days (" + iNumberDayOfLabourCostWorthy + " days) based on the labour information provided.";
                    LOG.debug("LabourCostBusinessRule failed: Number of hire days {} > Number of Labour cost worthy {}", iNumberOfHireDay, iNumberDayOfLabourCostWorthy);
                }
                else {
                    LOG.debug("LabourCostBusinessRule passed.");
                }

                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            } else {
                LOG.debug("Insufficient information to perform  LabourCostBusinessRule - rule skipped.");
                narrative = "Insufficient information to perform labour cost rule.";
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);

            }

        } else {
            LOG.debug("LabourCostBusinessRule skipped.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        return res;
    }

    private boolean isRequiredToValidateByBRE(Claim claim) {

        boolean bFlag = true;

        if (claim.getHireMonitoringDetail() != null) {

            BigDecimal bLabourCost = BigDecimal.ZERO;
            BigDecimal iLabourHour = BigDecimal.ZERO;

            if (claim.getHireMonitoringDetail().getLabourCost() != null) {
                bLabourCost = claim.getHireMonitoringDetail().getLabourCost();
            }

            if (claim.getHireMonitoringDetail().getLabourHour() != null) {
                iLabourHour = claim.getHireMonitoringDetail().getLabourHour();
            }

            if ((bLabourCost.compareTo(BigDecimal.ZERO) < 1) && (iLabourHour.compareTo(BigDecimal.ZERO) < 1)) {
                bFlag = false;
            }

        } else {
            bFlag = false;
        }

        return bFlag;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "022";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
