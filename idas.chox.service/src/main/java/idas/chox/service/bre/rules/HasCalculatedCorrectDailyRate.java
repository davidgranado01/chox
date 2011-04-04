package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;
import java.util.Date;

public class HasCalculatedCorrectDailyRate implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(HasCalculatedCorrectDailyRate.class);
    private VehicleClassPriceService vehicleClassPriceService;
    private InsurerChorganisationService insurerChorganisationService;

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }
    String narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        InsurerChorganisation insurerChorganisation = insurerChorganisationService.getInsurerChorganisation(claim.getInsurer().getId(), claim.getChorganisation().getId());

        LOG.debug("Applying HasCalculatedCorrectDailyRate rule to claim '{}'.", claim.getChoReference());

        if (claim.getBreBand().isHasCalculatedCorrectDailyRate() && claim.getVehicleHire() != null) {

            VehicleClass vehicleClass = claim.getVehicleHire().getVehicleClass();
            // removed this vehicle class validation checking for bug 876
          //  if (VehicleClassHelper.isVehicleClassValid(vehicleClass)) {
                Boolean isTclass = false;
                BigDecimal age = BigDecimal.ZERO;
                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
                BigDecimal allowedDailyRate = BigDecimal.ZERO;
                BigDecimal vehicleClassPrice = null;
                try {
                    // if hire vehicle class is a T or PT class, and the customer's vehicle is also a T or PT class,
                    // then the price will depend on the age of the customers vehicle
                    if (vehicleClass != null && VehicleClass.isTOrPTClass(vehicleClass.getName()) && claim.getCustomer()!= null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())
                            &&VehicleClass.isTOrPTClass(claim.getCustomer().getVehicleClass().getName())) {
                        isTclass = true;
                        /* Determine age of vehicle at gire start*/
                        Date firstRegistration = claim.getCustomer().getHpiFirstRegistration();
                        Date hireStart = claim.getVehicleHire().getHireStart();
                        if (firstRegistration != null && hireStart != null) {
                            age = new BigDecimal(DateHelper.DifferenceInYears(hireStart, firstRegistration));
                            vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart(), age, claim.getInsurer().getId(), claim.getChorganisation().getId());
                            LOG.debug("Got vehicle class price {} for vehicle of {} years old", vehicleClassPrice, age);
                        } else {
                            LOG.warn("Cannot deternine age of car for T vehicle class check: firstReg={}, hireStart={}", firstRegistration, hireStart);
                        }
                    }
                    if (vehicleClassPrice == null) {
                        vehicleClassPrice = vehicleClassPriceService.getPrice(vehicleClass, claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
                    }
                } catch (Exception ex) {
                    vehicleClassPrice = BigDecimal.ZERO;
                    LOG.info("Vehicle Class Price set to 0.0 as no price found for vehicle class {} (Supplier ref='{}')", vehicleClass.getName(), claim.getChoReference());
                }
                try {
                    allowedDailyRate = vehicleClassPrice.add(claim.getBreBand().getHireRateChargeTolerance());
                } catch (Exception ex) {
                    LOG.info("Cannot determine allowed daily rate - using £0.00: {}", ex.getMessage());
                    allowedDailyRate = BigDecimal.ZERO;
                }
                BigDecimal dailyHireRateCharged;
                try {
                     dailyHireRateCharged  = cCalc.getDailyHireRateCharged();
                } catch (Exception ex) {
                    LOG.info("Cannot determine  daily rate charged - using £0.00: {}", ex.getMessage());
                    dailyHireRateCharged = BigDecimal.ZERO;
                }
                LOG.debug("Comparing dailyHireRateCharged={} to allowedDailyRate={}", dailyHireRateCharged, allowedDailyRate);
                boolean success = dailyHireRateCharged.compareTo(allowedDailyRate) <= 0;

                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    LOG.debug("Rule passed: Daily rate billed for replacement vehicle class does not exceed ABI rate.");
                    narrative = "";
                } else {
                    LOG.debug("Rule failed: Daily rate billed of £ {} for replacement vehicle class exceeds ABI rate of £{}.", dailyHireRateCharged, allowedDailyRate);
//                    narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";
                    if(claim.isTpiClaim()&& vehicleClass.getName().toUpperCase().equalsIgnoreCase("UNATTACHED")){
                        narrative = "BRE Rule Failed Ð The CHO has provided a replacement vehicle that is outside of the ABI GTA vehicle class categories, please review." ;
                            }
                    else if (isTclass) {
                        if (claim.getBreBand().isUseSupplierRates()) {

                            narrative = "The daily rate billed of £" + dailyHireRateCharged.setScale(2, BigDecimal.ROUND_HALF_UP) + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed supplier rate of £" + allowedDailyRate.setScale(2, BigDecimal.ROUND_HALF_UP)+ "." ;
                        } else {
                            narrative = "The daily rate billed of £" + dailyHireRateCharged.setScale(2, BigDecimal.ROUND_HALF_UP) + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed ABI rate of £" + allowedDailyRate.setScale(2, BigDecimal.ROUND_HALF_UP)+ "." ;
                        }
                    } else {
                        if (claim.getBreBand().isUseSupplierRates()) {
                            narrative = "The daily rate billed of £" + dailyHireRateCharged.setScale(2, BigDecimal.ROUND_HALF_UP) + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed supplier rate of £" + allowedDailyRate.setScale(2, BigDecimal.ROUND_HALF_UP) + ".";
                        } else {
                            narrative = "The daily rate billed of £" + dailyHireRateCharged.setScale(2, BigDecimal.ROUND_HALF_UP) + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed ABI rate of £" + allowedDailyRate.setScale(2, BigDecimal.ROUND_HALF_UP) + ".";
                        }
                    }
                }
//          removed this else{} condition for vehicle class validation checking as per bug 876
//            } else {
//                LOG.debug("Vehicle class is not valid.");
//                narrative = "Vehicle Hire vehicle class is not specified.";
//                res.setResult(RuleEvaluationResult.RuleSkipped);
//            }

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
