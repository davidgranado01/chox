package idas.chox.service.bre.rules;

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
import java.math.BigDecimal;

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
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));
        LOG.debug("Applying setVehicleClassPriceService rule to claim '{}'.", claim.getChoReference());

        if (claim.getBreBand().isHasAllowedVehicleClass() && claim.getVehicleHire() != null) {

            if (claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {

                VehicleClass vehicleClass = claim.getCustomer().getVehicleClass();
                BigDecimal vehicleClassPrice = new BigDecimal(0.00);
                BigDecimal vehicleHireClassPrice = new BigDecimal(0.00);
                try {
                    vehicleClassPrice = vehicleClassPriceService.getPrice(claim.getClaimType(), vehicleClass, claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
                } catch (Exception ex) {
                    LOG.debug("Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", vehicleClass.getName(), claim.getChoReference());
                }
                try {
                    vehicleHireClassPrice = vehicleClassPriceService.getPrice(claim.getClaimType(), claim.getVehicleHire().getVehicleClass(), claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
                } catch (Exception ex) {
                    LOG.debug("Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", claim.getVehicleHire().getVehicleClass(), claim.getChoReference());
                }
                LOG.debug("Comparing vehicleHireClassPrice={} to vehicleClassPrice={}", vehicleHireClassPrice, vehicleClassPrice);
                boolean success = vehicleHireClassPrice.compareTo(vehicleClassPrice) <= 0;
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    LOG.debug("Rule passed: Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.");
                    narrative = "";
                } else {

                        narrative = "The vehicle class allocated for the hire (" + claim.getVehicleHire().getVehicleClass().getName() + ") is not a like for like match on the customer's vehicle class (" + claim.getCustomer().getVehicleClass().getName() + ").";
                        LOG.debug("Rule failed: {}", narrative);
                }

            } else {
                narrative = "Customer vehicle class is not specified.";
                LOG.debug("Rule skipped: {}", narrative);
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
