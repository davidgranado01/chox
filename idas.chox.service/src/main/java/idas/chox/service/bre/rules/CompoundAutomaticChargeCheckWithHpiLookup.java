package idas.chox.service.bre.rules;

import java.util.Date;
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
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.bre.util.ClaimCalcHelper;
import idas.chox.service.bre.util.VehicleClassHelper;

/**
 *
 * @author John
 */
public class CompoundAutomaticChargeCheckWithHpiLookup implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(CompoundAutomaticChargeCheckWithHpiLookup.class);
    private String narrative = "The CHO is charging an automatic fee for the hire and the HPI lookup did not identify the hire vehicle to be an automatic, please review need.";
    private VehicleClassPriceService vehicleClassPriceService;

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isCompoundAutomaticChargeCheckHpiLookup()
                && claim.getVehicleHire() != null && claim.getVehicleHire().getVehicleClass() != null
                && claim.getVehicleHire().getHpiVehicleTransmission() != null
                && claim.getVehicleHire().getVehicleClass().getName().endsWith("A")
                && !claim.getVehicleHire().getVehicleClass().getName().endsWith("ESTA")
                && !claim.getVehicleHire().getHpiVehicleTransmission().toLowerCase().contains("auto")) {
            VehicleClass vehicleClass = claim.getVehicleHire().getVehicleClass();
            Boolean isTclass = false;
            BigDecimal age;
            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
            BigDecimal allowedDailyRate = BigDecimal.ZERO;
            BigDecimal vehicleClassPrice = null;
            try {
                // if hire vehicle class is a T or PT class, and the customer's vehicle is also a T or PT class,
                // then the price will depend on the age of the hire vehicle
                if (VehicleClass.isTOrPTClass(vehicleClass.getName()) && claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())
                        && VehicleClass.isTOrPTClass(claim.getCustomer().getVehicleClass().getName())) {
                    isTclass = true;
                    /* Determine age of hire vehicle at hire start*/
                    Date firstRegistration = claim.getVehicleHire().getHpiFirstRegistration();
                    Date hireStart = claim.getVehicleHire().getHireStart();
                    if (firstRegistration != null && hireStart != null) {
                        age = new BigDecimal(DateHelper.differenceInYears(hireStart, firstRegistration));
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
                vehicleClassPrice = BigDecimal.ZERO.setScale(2);
                LOG.debug("Vehicle Class Price set to 0.00 as no price found for Supplier ref='{}': ", claim.getChoReference(), ex);

            }
            try {
                allowedDailyRate = vehicleClassPrice.add(claim.getBreBand().getHireRateChargeTolerance()).setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (Exception ex) {
                LOG.warn("Cannot determine allowed daily rate for claim '{}' - using £0.00: {}", claim.getChoReference(), ex.getMessage());
                allowedDailyRate = BigDecimal.ZERO.setScale(2);
            }
            BigDecimal dailyHireRateCharged;
            try {
                dailyHireRateCharged = cCalc.getDailyHireRateCharged().setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (Exception ex) {
                LOG.warn("Cannot determine  daily rate charged for claim '{}' - using £0.00: {}", claim.getChoReference(), ex.getMessage());
                dailyHireRateCharged = BigDecimal.ZERO.setScale(2);
            }
            LOG.debug("Comparing dailyHireRateCharged={} to allowedDailyRate={}", dailyHireRateCharged, allowedDailyRate);
            if (dailyHireRateCharged.compareTo(allowedDailyRate) >= 0) {
                res.setResult(RuleEvaluationResult.RULE_FAILED);
            } else {
                narrative = "";
                res.setResult(RuleEvaluationResult.RULE_PASSED);
            }
        } else {
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
        return "081";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
