package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;

public class HasCalculatedCorrectDailyRate implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(HasCalculatedCorrectDailyRate.class);
    private VehicleClassPriceService vehicleClassPriceService;

    
    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }
    String narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            res.setIsVisibleToCHO(true);
        } else {
            res.setIsVisibleToCHO(false);
        }
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        LOG.debug("Applying HasCalculatedCorrectDailyRate rule to claim '{}'.", claim.getChoReference());

        if (claim.getBreBand().isHasCalculatedCorrectDailyRate() && claim.getVehicleHire() != null) {

            VehicleClass vehicleClass = claim.getVehicleHire().getVehicleClass();
            // removed this vehicle class validation checking for bug#876
            // added vehicle class null check for bug#980
            if (vehicleClass != null) {
                Boolean isTclass = false;
                BigDecimal age = null;
                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
                BigDecimal allowedDailyRate = BigDecimal.ZERO;
                BigDecimal vehicleClassPrice = null;
                try {
                    // if hire vehicle class is a T or PT class, and the customer's vehicle is also a T or PT class,
                    // then the price will depend on the age of the customers vehicle
                    if (VehicleClass.isTOrPTClass(vehicleClass.getName()) && claim.getCustomer()!= null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())
                            &&VehicleClass.isTOrPTClass(claim.getCustomer().getVehicleClass().getName())) {
                        isTclass = true;
                        /* Determine age of hire vehicle at hire start*/
                        Date firstRegistration = claim.getVehicleHire().getHpiFirstRegistration();
                        Date hireStart = claim.getVehicleHire().getHireStart();
                        if (firstRegistration != null && hireStart != null) {
                            age = new BigDecimal(DateHelper.DifferenceInYears(hireStart, firstRegistration));
                            vehicleClassPrice = vehicleClassPriceService.getPrice(claim.getClaimType(), vehicleClass, claim.getVehicleHire().getHireStart(), age, claim.getInsurer().getId(), claim.getChorganisation().getId());
                            LOG.debug("Got vehicle class price {} for vehicle of {} years old", vehicleClassPrice, age);
                        } else {
                            LOG.warn("Cannot determine age of car for T vehicle class check: firstReg={}, hireStart={}", firstRegistration, hireStart);
                            
                        }
                    }
                    if (vehicleClassPrice == null) {
                        vehicleClassPrice = vehicleClassPriceService.getPrice(claim.getClaimType(), vehicleClass, claim.getVehicleHire().getHireStart(), claim.getInsurer().getId(), claim.getChorganisation().getId());
                       
                    }
                } catch (Exception ex) {
                    vehicleClassPrice = BigDecimal.ZERO;
                    LOG.debug("Vehicle Class Price set to 0.0 as no price found for Supplier ref='{}')", claim.getChoReference());
                    
                }
                try {
                    allowedDailyRate = vehicleClassPrice.add(claim.getBreBand().getHireRateChargeTolerance()).setScale(2, BigDecimal.ROUND_HALF_UP);
                } catch (Exception ex) {
                    LOG.warn("Cannot determine allowed daily rate for claim '{}' - using £0.00: {}", claim.getChoReference(), ex.getMessage());
                    allowedDailyRate = BigDecimal.ZERO.setScale(2);
                }
                BigDecimal dailyHireRateCharged;
                try {
                     dailyHireRateCharged  = cCalc.getDailyHireRateCharged().setScale(2, BigDecimal.ROUND_HALF_UP) ;
                } catch (Exception ex) {
                    LOG.warn("Cannot determine  daily rate charged for claim '{}' - using £0.00: {}", claim.getChoReference(), ex.getMessage());
                    dailyHireRateCharged = BigDecimal.ZERO.setScale(2);
                }
                LOG.debug("Comparing dailyHireRateCharged={} to allowedDailyRate={}", dailyHireRateCharged, allowedDailyRate);
                boolean success = dailyHireRateCharged.compareTo(allowedDailyRate) <= 0;

                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
                if (success) {
                    LOG.debug("Rule passed: Daily rate billed for replacement vehicle class does not exceed ABI rate.");
                    narrative = "";
                } else {
                    LOG.debug("Rule failed: Daily rate billed of £ {} for replacement vehicle class exceeds ABI rate of £{}.", dailyHireRateCharged, allowedDailyRate);
//                    narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";
                    if(ClaimType.isTPI(claim.getClaimType()) && vehicleClass.getName().equalsIgnoreCase("UNATTACHED")){
                        narrative = "BRE Rule Failed : The CHO has provided a replacement vehicle that is outside of the ABI GTA vehicle class categories, please review." ;
                            }
                    else if (isTclass) {
                        if (claim.getBreBand().isUseSupplierRates() || ClaimType.isSubscriber(claim.getClaimType()) || ClaimType.isFixedFee(claim.getClaimType())) {
                            if (age == null) {
                                narrative = "The daily rate billed of £" + dailyHireRateCharged + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed supplier rate of £" + allowedDailyRate + "." ;
                            }
                            else {
                                narrative = "The daily rate billed of £" + dailyHireRateCharged + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed supplier rate of £" + allowedDailyRate + " based on the age of the replacement vehicle, which is " + age.setScale(1, RoundingMode.HALF_UP) + " years old." ;
                            }
                        } else {
                            if (age == null) {
                                narrative = "The daily rate billed of £" + dailyHireRateCharged + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed ABI rate of £" + allowedDailyRate + " (note that the age of the vehicle could not be determined and so the lower rate for the vehicle class was used)." ;
                            }
                            else {
                                narrative = "The daily rate billed of £" + dailyHireRateCharged + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed ABI rate of £" + allowedDailyRate + " based on the age of the replacement vehicle, which is " + age.setScale(1, RoundingMode.HALF_UP) + " years old." ;
                            }
                        }
                    } else {
                        if (claim.getBreBand().isUseSupplierRates() || ClaimType.isSubscriber(claim.getClaimType()) || ClaimType.isFixedFee(claim.getClaimType())) {
                            narrative = "The daily rate billed of £" + dailyHireRateCharged + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed supplier rate of £" + allowedDailyRate + ".";
                        } else {
                            narrative = "The daily rate billed of £" + dailyHireRateCharged + " for the replacement vehicle class " + vehicleClass.getName() + " exceeds the allowed ABI rate of £" + allowedDailyRate + ".";
                        }
                    }
                }
            } else {
                LOG.debug("No Vehicle class supplied for claim '{}'.", claim.getChoReference());
                if (ClaimType.isTPI(claim.getClaimType())) {
                    narrative = "Vehicle Hire vehicle class is not specified.";
                    res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                } else {
                    LOG.error("Non-TPI claim has no vehicle attached: cho ref='{}'", claim.getChoReference());
                    narrative = "Vehicle Hire vehicle class is not specified.";
                    res.setResult(RuleEvaluationResult.RULE_FAILED);
                }
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
        return "002";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isSubscriber(claimType)) {
            return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
