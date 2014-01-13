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

public class MaximumLabourRateStandard implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(MaximumLabourRateStandard.class);
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'MaximumLabourRateStandard' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        String customerVehicleClass = null;
        
        if (claim.getCustomer() != null && claim.getCustomer().getVehicleClass() != null) {
            customerVehicleClass = claim.getCustomer().getVehicleClass().getName();
        }
        
        if (claim.getBreBand().isMaximumLabourRateStandardCheck() && checkCustomersVehicleClass(customerVehicleClass)
                && claim.getHireMonitoringDetail() != null
                && claim.getHireMonitoringDetail().getLabourRate() != null) {


            LOG.debug("Applying 'MaximumLabourRateStandard' Business Rule to claim {}.", claim.getChoReference());

            boolean success = true;

            BigDecimal maxLabourRate = claim.getBreBand().getMaxAllowedLabourStandardRate();
            BigDecimal labourRate = claim.getHireMonitoringDetail().getLabourRate();

 
            if (labourRate.compareTo(maxLabourRate) > 0) {
                success = false;
                narrative = "The CHO is charging £" + labourRate.toString()
                        + " per labour hour which is more than the maximum labour rate per hour of £"
                        + maxLabourRate + " for standard vehicles & vans, please review.";

            } else {
                narrative = "";
                LOG.debug("'MaximumLabourRateStandard' Business Rule Passed.");
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        } else {
            LOG.debug("'MaximumLabourRateStandard' Business Rule Skipped.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        LOG.debug("'MaximumLabourRateStandard' Business Rule Finished.");
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "087";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

    private boolean checkCustomersVehicleClass(String customerVehicleClass) {
        if (customerVehicleClass != null) {
            if (customerVehicleClass.startsWith("S1")
                    || customerVehicleClass.startsWith("S2")
                    || customerVehicleClass.startsWith("S3")
                    || customerVehicleClass.startsWith("S4")
                    || customerVehicleClass.startsWith("S5")
                    || customerVehicleClass.startsWith("S6")
                    || customerVehicleClass.startsWith("S7")
                    || customerVehicleClass.startsWith("M")
                    || customerVehicleClass.startsWith("M1")
                    || customerVehicleClass.startsWith("M2")
                    || customerVehicleClass.startsWith("M3")
                    || customerVehicleClass.startsWith("M4")
                    || customerVehicleClass.startsWith("F1")
                    || customerVehicleClass.startsWith("F2")
                    || customerVehicleClass.startsWith("F3")
                    || customerVehicleClass.equals("SP1")
                    || customerVehicleClass.equals("SP1A")
                    || customerVehicleClass.equals("SP1EST")
                    || customerVehicleClass.equals("SP1ESTA")
                    || customerVehicleClass.startsWith("SP2")
                    || customerVehicleClass.startsWith("SP3")
                    || customerVehicleClass.startsWith("T1")
                    || customerVehicleClass.startsWith("T2")
                    || customerVehicleClass.startsWith("T3")
                    || customerVehicleClass.startsWith("T4")
                    || customerVehicleClass.startsWith("B1")
                    || customerVehicleClass.startsWith("B2")
                    || customerVehicleClass.startsWith("B3")) {
                return true;
            }
        }
        return false;
    }
}
