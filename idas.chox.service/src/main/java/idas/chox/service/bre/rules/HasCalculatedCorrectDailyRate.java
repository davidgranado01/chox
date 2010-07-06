package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.data.services.VehicleClassPriceServiceImpl;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;

public class HasCalculatedCorrectDailyRate implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(HasCalculatedCorrectDailyRate.class);
    private VehicleClassPriceService vehicleClassPriceService ;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        LOG.debug("Vehicle Class Price service has been set.");
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    String narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";

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

        if (claim.getBreBand().isHasCalculatedCorrectDailyRate()) {

            VehicleClass vehicleClass = claim.getVehicleHire().getVehicleClass();
            if (VehicleClassHelper.isVehicleClassValid(vehicleClass)) {

                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
                BigDecimal allowedDailyRate = new BigDecimal(0.00);
                BigDecimal vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart());
                allowedDailyRate = vehicleClassPrice.add(claim.getBreBand().getHireRateChargeTolerance());

                boolean success = cCalc.getDailyHireRateCharged().compareTo(allowedDailyRate) <= 0;

                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    narrative = "";
                }else{
                    narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";
                }

            } else {

                narrative = "Vehicle Hire vehicle class is not specified.";
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
        return "002";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
