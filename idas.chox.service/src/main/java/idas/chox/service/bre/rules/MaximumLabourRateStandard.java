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
                        + maxLabourRate + "for standard vehicles & vans, please review.";

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
            if (customerVehicleClass.equals("S1")
                    || customerVehicleClass.equals("S2")
                    || customerVehicleClass.equals("S3")
                    || customerVehicleClass.equals("S4")
                    || customerVehicleClass.equals("S5")
                    || customerVehicleClass.equals("S6")
                    || customerVehicleClass.equals("S7")
                    || customerVehicleClass.equals("M")
                    || customerVehicleClass.equals("M1")
                    || customerVehicleClass.equals("M2")
                    || customerVehicleClass.equals("M3")
                    || customerVehicleClass.equals("M4")
                    || customerVehicleClass.equals("F1")
                    || customerVehicleClass.equals("F2")
                    || customerVehicleClass.equals("F3")
                    || customerVehicleClass.equals("SP1")
                    || customerVehicleClass.equals("SP2")
                    || customerVehicleClass.equals("SP3")
                    || customerVehicleClass.equals("T1")
                    || customerVehicleClass.equals("T2")
                    || customerVehicleClass.equals("T3")
                    || customerVehicleClass.equals("T4")
                    || customerVehicleClass.equals("B1")
                    || customerVehicleClass.equals("B2")
                    || customerVehicleClass.equals("B3")) {
                return true;
            }
        }
        return false;
    }
}
