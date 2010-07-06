package idas.chox.service.bre.rules;

import org.springframework.beans.factory.annotation.Autowired;
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
import idas.chox.data.services.VehicleClassPriceServiceImpl;
import java.math.BigDecimal;

public class HasAllowedVehicleClass implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(HasAllowedVehicleClass.class);
    private VehicleClassPriceService vehicleClassPriceService ;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        LOG.debug("Vehicle Class Price service has been set.");
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    String narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        if (vehicleClassPriceService == null) {
            LOG.error("vehicleClassPriceService has not been injected!!");
            vehicleClassPriceService = new VehicleClassPriceServiceImpl();
        }
        else
            LOG.info("vehicleClassPriceService has been injected!!");

        if (claim.getBreBand().isHasAllowedVehicleClass()) {

            if (claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {

                VehicleClass vehicleClass = claim.getCustomer().getVehicleClass();
                BigDecimal vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart());
                BigDecimal vehicleHireClassPrice = vehicleClassPriceService.getPrice(claim.getVehicleHire().getVehicleClass(), claim.getVehicleHire().getHireStart());
//                boolean success = claim.getVehicleHire().getVehicleClass().getPrice().compareTo(vehicleClass.getPrice()) <= 0;
                boolean success = vehicleHireClassPrice.compareTo(vehicleClassPrice) <= 0;
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    narrative = "";
                }else{
                    narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";
                }

            } else {

                narrative = "Customer vehicle class is not specified.";
                res.setResult(RuleEvaluationResult.RuleSkipped);

            }

        } else {

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
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
