package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;
import idas.chox.service.bre.util.VehicleClassHelper;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.util.DateHelper;
import java.math.BigDecimal;
import java.util.Date;

public class HasAllowedVehicleClass implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(HasAllowedVehicleClass.class);
    private VehicleClassPriceService vehicleClassPriceService ;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    String narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(claim.isTpiClaim());
        LOG.debug("Applying setVehicleClassPriceService rule to claim '{}'.", claim.getChoReference());

        if (claim.getBreBand().isHasAllowedVehicleClass() && claim.getVehicleHire() != null) {

            if (claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {

                Boolean isPTclass = false;
                BigDecimal age = BigDecimal.ZERO;
                VehicleClass vehicleClass = claim.getCustomer().getVehicleClass();
                BigDecimal vehicleClassPrice = new BigDecimal(0.00);
                BigDecimal vehicleHireClassPrice = new BigDecimal(0.00);
                if (VehicleClass.isPTClass(vehicleClass.getName())) {
                    isPTclass = true;
                    Date firstRegistration = claim.getCustomer().getHpiFirstRegistration();
                    Date hireStart = claim.getVehicleHire().getHireStart();
                    if (firstRegistration != null && hireStart != null)
                        age = new BigDecimal(DateHelper.DifferenceInYears(hireStart, firstRegistration));
                }
                try {
                    if (isPTclass)
                        vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart(), age, claim.getInsurer().getId(), claim.getChorganisation().getId());
                    else
                        vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
                } catch (Exception ex) {
                    LOG.debug("Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", vehicleClass.getName(), claim.getChoReference());
                }
                try {
                    vehicleHireClassPrice = vehicleClassPriceService.getPrice(claim.getVehicleHire().getVehicleClass(), claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
                } catch (Exception ex) {
                    LOG.debug("Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", claim.getVehicleHire().getVehicleClass(), claim.getChoReference());
                }
//                boolean success = claim.getVehicleHire().getVehicleClass().getPrice().compareTo(vehicleClass.getPrice()) <= 0;
                LOG.debug("Comparing vehicleHireClassPrice={} to vehicleClassPrice={}", vehicleHireClassPrice, vehicleClassPrice);
                boolean success = vehicleHireClassPrice.compareTo(vehicleClassPrice) <= 0;
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    LOG.debug("Rule passed: Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.");
                    narrative = "";
                }else{
                    if (isPTclass) {
                        LOG.debug("Rule failed: Vehicle class allocated for hire is not a like for like match for the customer's T-Class vehicle.");
//                        narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";
                        narrative = "The vehicle class allocated for the hire (" + claim.getVehicleHire().getVehicleClass().getName() + ") is not a like for like match on the customer's vehicle class (" + claim.getCustomer().getVehicleClass().getName() + "). This is possibly due to the age of the customers car, which is " + age.setScale(2, BigDecimal.ROUND_HALF_UP) + " years old.";

                    }
                    else {
                        LOG.debug("Rule failed: Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.");
//                        narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";
                        narrative = "The vehicle class allocated for the hire (" + claim.getVehicleHire().getVehicleClass().getName() + ") is not a like for like match on the customer's vehicle class (" + claim.getCustomer().getVehicleClass().getName() + ").";
                    }
                }

            } else {
                LOG.debug("Rule skipped: Customer vehicle class is not specified.");
                narrative = "Customer vehicle class is not specified.";
                res.setResult(RuleEvaluationResult.RuleSkipped);
            }

        } else {
            LOG.debug("Rule not switched on.");
            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);
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
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
