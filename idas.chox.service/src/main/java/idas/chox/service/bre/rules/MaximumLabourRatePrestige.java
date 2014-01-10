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
        LOG.debug("Applying rule 'MaximumLabourRatePrestige' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        String customerVehicleClass = null;
        
        if (claim.getCustomer() != null && claim.getCustomer().getVehicleClass() != null) {
            customerVehicleClass = claim.getCustomer().getVehicleClass().getName();
        }

        if (claim.getBreBand().isMaximumLabourRatePrestigeCheck() && checkCustomersVehicleClass(customerVehicleClass)
                && claim.getHireMonitoringDetail() != null
                && claim.getHireMonitoringDetail().getLabourRate() != null) {

            LOG.debug("Applying 'MaximumLabourRatePrestige' Business Rule to claim {}.", claim.getChoReference());

            boolean success = true;

            BigDecimal maxLabourRate = claim.getBreBand().getMaxAllowedLabourPrestigeRate();
            BigDecimal labourRate = claim.getHireMonitoringDetail().getLabourRate();

 
            if (labourRate.compareTo(maxLabourRate) > 0) {
                success = false;
                narrative = "The CHO is charging £" + labourRate.toString()
                        + " per labour hour which is more than the maximum labour rate per hour of £"
                        + maxLabourRate + " for prestige & special vehicles & vans, please review.";

            } else {
                narrative = "";
                LOG.debug("'MaximumLabourRatePrestige' Business Rule Passed.");
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        } else {
            LOG.debug("'MaximumLabourRatePrestige' Business Rule Skipped.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        LOG.debug("'MaximumLabourRatePrestige' Business Rule Finished.");
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

    private boolean checkCustomersVehicleClass(String customerVehicleClass) {
        if (customerVehicleClass != null) {
            if (customerVehicleClass.startsWith("M5")
                    || customerVehicleClass.startsWith("M6")
                    || customerVehicleClass.startsWith("F4")
                    || customerVehicleClass.startsWith("F5")
                    || customerVehicleClass.startsWith("F6")
                    || customerVehicleClass.startsWith("F7")
                    || customerVehicleClass.startsWith("F8")
                    || customerVehicleClass.startsWith("F9")
                    || customerVehicleClass.equals("P1")
                    || customerVehicleClass.equals("P1A")
                    || customerVehicleClass.equals("P1EST")
                    || customerVehicleClass.equals("P1ESTA")
                    || customerVehicleClass.startsWith("P2")
                    || customerVehicleClass.startsWith("P3")
                    || customerVehicleClass.startsWith("P4")
                    || customerVehicleClass.startsWith("P5")
                    || customerVehicleClass.startsWith("P6")
                    || customerVehicleClass.startsWith("P7")
                    || customerVehicleClass.startsWith("P8")
                    || customerVehicleClass.startsWith("P9")
                    || customerVehicleClass.startsWith("P10")
                    || customerVehicleClass.startsWith("P11")
                    || customerVehicleClass.startsWith("P12")
                    || customerVehicleClass.startsWith("P13")
                    || customerVehicleClass.startsWith("SP4")
                    || customerVehicleClass.startsWith("SP5")
                    || customerVehicleClass.startsWith("SP6")
                    || customerVehicleClass.startsWith("SP7")
                    || customerVehicleClass.startsWith("SP8")
                    || customerVehicleClass.startsWith("SP9")
                    || customerVehicleClass.startsWith("SP10")
                    || customerVehicleClass.startsWith("SP11")
                    || customerVehicleClass.startsWith("SP12")
                    || customerVehicleClass.startsWith("SP13")
                    || customerVehicleClass.startsWith("PV1")
                    || customerVehicleClass.startsWith("PV2")
                    || customerVehicleClass.startsWith("PV3")
                    || customerVehicleClass.startsWith("PV4")
                    || customerVehicleClass.startsWith("PV5")
                    || customerVehicleClass.startsWith("PV6")
                    || customerVehicleClass.startsWith("CV1")
                    || customerVehicleClass.startsWith("CV2")
                    || customerVehicleClass.startsWith("CV3")
                    || customerVehicleClass.startsWith("CV4")
                    || customerVehicleClass.startsWith("RV1")
                    || customerVehicleClass.startsWith("RV2")
                    || customerVehicleClass.startsWith("CP1")
                    || customerVehicleClass.startsWith("CP2")
                    || customerVehicleClass.startsWith("CP3")
                    || customerVehicleClass.startsWith("CS1")
                    || customerVehicleClass.startsWith("CS2")
                    || customerVehicleClass.startsWith("CS3")
                    || customerVehicleClass.startsWith("CS4")
                    || customerVehicleClass.startsWith("CS5")
                    || customerVehicleClass.startsWith("CM1")
                    || customerVehicleClass.startsWith("CM2")
                    || customerVehicleClass.startsWith("CM3")
                    || customerVehicleClass.startsWith("T5")
                    || customerVehicleClass.startsWith("T6")
                    || customerVehicleClass.startsWith("T7")
                    || customerVehicleClass.startsWith("T8")
                    || customerVehicleClass.startsWith("T9")
                    || customerVehicleClass.startsWith("PT9")
                    || customerVehicleClass.startsWith("T10")
                    || customerVehicleClass.startsWith("T11")
                    || customerVehicleClass.startsWith("T12")
                    || customerVehicleClass.startsWith("T13")
                    || customerVehicleClass.startsWith("PT13")
                    || customerVehicleClass.startsWith("T14")
                    || customerVehicleClass.startsWith("B4")
                    || customerVehicleClass.startsWith("B5")
                    || customerVehicleClass.startsWith("B6")) {
                return true;
            }
        }
        return false;
    }
}
