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
import idas.chox.core.model.VehicleClass;
import idas.chox.service.bre.util.VehicleClassHelper;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.service.bre.util.ClaimCalcHelper;

public class HasAllowedVehicleClass implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(HasAllowedVehicleClass.class);
    private VehicleClassPriceService vehicleClassPriceService;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }
    String narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        LOG.debug("Applying setVehicleClassPriceService rule to claim '{}'.", claim.getChoReference());

        if (claim.getBreBand().isHasAllowedVehicleClass() && claim.getVehicleHire() != null) {

            if (claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {

                /*
                 * ToDo Item  6.9.3 Like For Like Rule Linked To Daily Rate Of Customer's Class
                 */
                // First, get the daily rate charged
                VehicleClass customerVehicleClass = claim.getCustomer().getVehicleClass();
                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
                BigDecimal dailyHireRateCharged;
                try {
                    dailyHireRateCharged = cCalc.getDailyHireRateCharged();
                } catch (Exception ex) {
                    LOG.warn("Cannot determine  daily rate charged for claim '{}' - using £0.00: {}", claim.getChoReference(), ex.getMessage());
                    dailyHireRateCharged = BigDecimal.ZERO;
                }
                // Next, get the allowed daily rate for the customers vehicle class
                BigDecimal customerVehicleClassPrice = BigDecimal.ZERO;
                try {
                    customerVehicleClassPrice = vehicleClassPriceService.getPrice(claim.getClaimType(), customerVehicleClass, claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
                } catch (Exception ex) {
                    LOG.warn("Customer's Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", claim.getCustomer().getVehicleClass(), claim.getChoReference());
                    customerVehicleClassPrice = BigDecimal.ZERO;
                }
                // Add in the hire-rate tolerance - No!! Not Needed
//                customerVehicleClassPrice = customerVehicleClassPrice.add(claim.getBreBand().getHireRateChargeTolerance());
                if (dailyHireRateCharged.compareTo(customerVehicleClassPrice) > 0) {

                    BigDecimal vehicleHireClassPrice = BigDecimal.ZERO;
                    try {
                        vehicleHireClassPrice = vehicleClassPriceService.getPrice(claim.getClaimType(), claim.getVehicleHire().getVehicleClass(), claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
                    } catch (Exception ex) {
                        LOG.debug("Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", claim.getVehicleHire().getVehicleClass(), claim.getChoReference());
                    }
                    LOG.debug("Comparing vehicleHireClassPrice={} to vehicleClassPrice={}", vehicleHireClassPrice, customerVehicleClassPrice);
                    boolean success = vehicleHireClassPrice.compareTo(customerVehicleClassPrice) <= 0;
                    res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
                    if (success) {
                        LOG.debug("Rule passed: Vehicle class allocated for hire is a like for like match on the customer's vehicle class.");
                        narrative = "";
                    } else {

                        narrative = "The vehicle class allocated for the hire (" + claim.getVehicleHire().getVehicleClass().getName() + ") is not a like for like match on the customer's vehicle class (" + claim.getCustomer().getVehicleClass().getName() + ").";
                        LOG.debug("Rule failed: {}", narrative);
                    }
                } else {
                    narrative = "The calculated daily rate charged is less than or equal to the allowed daily rate based upon the customers vehicle class.";
                    LOG.debug("Rule skipped: {}", narrative);
                    res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                }
            } else {
                narrative = "Customer vehicle class is not specified.";
                LOG.debug("Rule skipped: {}", narrative);
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);
            }

        } else {
            LOG.debug("Rule not switched on.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);
        }

        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "001";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isSubscriber(claimType)) {
            return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
