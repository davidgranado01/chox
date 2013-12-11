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
            if (customerVehicleClass.equals("M5")
                    || customerVehicleClass.equals("M6")
                    || customerVehicleClass.equals("F4")
                    || customerVehicleClass.equals("F5")
                    || customerVehicleClass.equals("F6")
                    || customerVehicleClass.equals("F7")
                    || customerVehicleClass.equals("F8")
                    || customerVehicleClass.equals("F9")
                    || customerVehicleClass.equals("P1")
                    || customerVehicleClass.equals("P2")
                    || customerVehicleClass.equals("P3")
                    || customerVehicleClass.equals("P4")
                    || customerVehicleClass.equals("P5")
                    || customerVehicleClass.equals("P6")
                    || customerVehicleClass.equals("P7")
                    || customerVehicleClass.equals("P8")
                    || customerVehicleClass.equals("P9")
                    || customerVehicleClass.equals("P10")
                    || customerVehicleClass.equals("P11")
                    || customerVehicleClass.equals("P12")
                    || customerVehicleClass.equals("P13")
                    || customerVehicleClass.equals("SP4")
                    || customerVehicleClass.equals("SP5")
                    || customerVehicleClass.equals("SP6")
                    || customerVehicleClass.equals("SP7")
                    || customerVehicleClass.equals("SP8")
                    || customerVehicleClass.equals("SP9")
                    || customerVehicleClass.equals("SP10")
                    || customerVehicleClass.equals("SP11")
                    || customerVehicleClass.equals("SP12")
                    || customerVehicleClass.equals("SP13")
                    || customerVehicleClass.equals("PV1")
                    || customerVehicleClass.equals("PV2")
                    || customerVehicleClass.equals("PV3")
                    || customerVehicleClass.equals("PV4")
                    || customerVehicleClass.equals("PV5")
                    || customerVehicleClass.equals("PV6")
                    || customerVehicleClass.equals("CV1")
                    || customerVehicleClass.equals("CV2")
                    || customerVehicleClass.equals("CV3")
                    || customerVehicleClass.equals("CV4")
                    || customerVehicleClass.equals("RV1")
                    || customerVehicleClass.equals("RV2")
                    || customerVehicleClass.equals("CP1")
                    || customerVehicleClass.equals("CP2")
                    || customerVehicleClass.equals("CP3")
                    || customerVehicleClass.equals("CS1")
                    || customerVehicleClass.equals("CS2")
                    || customerVehicleClass.equals("CS3")
                    || customerVehicleClass.equals("CS4")
                    || customerVehicleClass.equals("CS5")
                    || customerVehicleClass.equals("CM1")
                    || customerVehicleClass.equals("CM2")
                    || customerVehicleClass.equals("CM3")
                    || customerVehicleClass.equals("T5")
                    || customerVehicleClass.equals("T6")
                    || customerVehicleClass.equals("T7")
                    || customerVehicleClass.equals("T8")
                    || customerVehicleClass.equals("T9")
                    || customerVehicleClass.equals("PT9")
                    || customerVehicleClass.equals("T10")
                    || customerVehicleClass.equals("T11")
                    || customerVehicleClass.equals("T12")
                    || customerVehicleClass.equals("T13")
                    || customerVehicleClass.equals("PT13")
                    || customerVehicleClass.equals("T14")
                    || customerVehicleClass.equals("B4")
                    || customerVehicleClass.equals("B5")
                    || customerVehicleClass.equals("B6")) {
                return true;
            }
        }
        return false;
    }
}
