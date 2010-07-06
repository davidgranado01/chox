package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;

public class HasCalculatedCorrectDailyRate implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(HasCalculatedCorrectDailyRate.class);
    private VehicleClassPriceService vehicleClassPriceService ;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    String narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        LOG.debug("Applying HasCalculatedCorrectDailyRate rule to claim '{}'.", claim.getChoReference());

        if (claim.getBreBand().isHasCalculatedCorrectDailyRate()) {

            VehicleClass vehicleClass = claim.getVehicleHire().getVehicleClass();
            if (VehicleClassHelper.isVehicleClassValid(vehicleClass)) {

                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
                BigDecimal allowedDailyRate = new BigDecimal(0.00);
                BigDecimal vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart());
                allowedDailyRate = vehicleClassPrice.add(claim.getBreBand().getHireRateChargeTolerance());
                BigDecimal dailyHireRateCharged = cCalc.getDailyHireRateCharged();
                LOG.debug("Comparing dailyHireRateCharged={} to allowedDailyRate={}", dailyHireRateCharged, allowedDailyRate);
                boolean success = dailyHireRateCharged.compareTo(allowedDailyRate) <= 0;

                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    LOG.debug("Rule passed: Daily rate billed for replacement vehicle class exceeds ABI rate.");
                    narrative = "";
                }else{
                    LOG.debug("Rule failed: Daily rate billed for replacement vehicle class exceeds ABI rate.");
                    narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";
                }

            } else {
                LOG.debug("Vehicle class is not valid.");
                narrative = "Vehicle Hire vehicle class is not specified.";
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
        return "002";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
