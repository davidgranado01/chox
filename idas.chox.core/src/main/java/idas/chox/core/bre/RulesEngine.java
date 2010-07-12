package idas.chox.core.bre;

import idas.chox.core.model.Claim;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesEngine {
    public static final Logger LOG = LoggerFactory.getLogger(RulesEngine.class);

    private List<IBusinessRule> businessRules;

    public RulesEngine() {       
//        businessRules.add(new HasAllowedVehicleClass);
//        businessRules.add(new HasCalculatedCorrectDailyRate);
//        businessRules.add(new HireNetDoesNotExceedVehicleClassHireNetCeiling);
//        businessRules.add(new HireDayCountDoesNotExceedBandHireDayCeiling);
//        businessRules.add(new HasCorrectHireGrossCalculation);
//        businessRules.add(new ActualHireDaysDoesNotExceedAllowableHireDays);
//        businessRules.add(new ActualHireDaysDoesNotExceedTotalLossInspection);
//        businessRules.add(new RepairGrossIsLessThanEstimatedTotalRepairAmount);
//        businessRules.add(new HasCorrectHireVatCalculation);
//        businessRules.add(new HasCorrectRepairVatCalculation);
//        businessRules.add(new HasCorrectRepairGrossCalculation);
//        businessRules.add(new HasCorrectTotalNet);
//        businessRules.add(new HasCorrectTotalVat);
//        businessRules.add(new HasCalculatedTotalGrossEqualSuppliedTotalGross);
//        businessRules.add(new HasCorrectDiscountForNonDA);
//        businessRules.add(new HandlingAmountAndDeductionBothEqualZeroForNonDA);
//        businessRules.add(new ClaimHasZeroDiscountForDA);
//        businessRules.add(new HandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero);
//        businessRules.add(new HasSuppliedCorrectTotalToPay);
//        businessRules.add(new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays);
//        businessRules.add(new LabourCostBusinessRule);
//        businessRules.add(new RepairNetDoesNotExceedVehicleClassRepairNetCeiling); //Emmanuel 18-09-2009
//        businessRules.add(new NumberOfHireDaysReconcile);            // RULE 024
//        businessRules.add(new CorrentAdminFee);                      // RULE 025
//        businessRules.add(new RepairBookedInDateOnFriday);           // RULE 026
//        businessRules.add(new FlaggedForManualInvoiceReview);        // RULE 027
//        businessRules.add(new AutomaticChargeCheck);                 // RULE 028
//        businessRules.add(new EstateChargeCheck);                    // RULE 029
//        businessRules.add(new NonStandardRiskInsurancePremiumCheck); // RULE 030
//        businessRules.add(new CDWChargeCheck);                       // RULE 031
//        businessRules.add(new SatelliteNavigationChargeCheck);       // RULE 032
//        businessRules.add(new BabySeatChargeCheck);                  // RULE 033
//        businessRules.add(new TowBarsChargeCheck);                   // RULE 034
//        businessRules.add(new RoofRackChargeCheck);                  // RULE 035
//        businessRules.add(new DeliveryOrCollectionChargeCheck);      // RULE 036
//        businessRules.add(new DualControlChargeCheck);               // RULE 037
//        businessRules.add(new RepairBookedInDateOnSaturday);         // RULE 038
//        businessRules.add(new RepairBookedInDateOnSunday);           // RULE 039
//        businessRules.add(new HireNetDoesNotExceedHireNetCeiling);       // RULE 040
//        businessRules.add(new RepairNetDoesNotExceedRepairNetCeiling);   // RULE 041
//        businessRules.add(new validateUniqueVehicleRegistrationNumber);
    }

    public RulesEngineResponse validate(Claim claim) {
        LOG.debug("Validating claim '{}'", claim.getChoReference());
        LOG.debug("Applying {} rules to claim", businessRules.size());
        RulesEngineResponse response = new RulesEngineResponse();
        for (IBusinessRule businessRule : businessRules) {
            LOG.debug("Applying rule '{}'", businessRule.getRuleId());
            RuleEvaluation ev = businessRule.applyToClaim(claim);
            response.addRuleEvaulation(ev);
            LOG.debug(businessRule.getNarrative() + " - " + ev.getResult() + " - " +businessRule.getRuleId());
        }
        return response;
    }

    public List<IBusinessRule> getBusinessRules() {
        return businessRules;
    }

    public void setBusinessRules(List<IBusinessRule> businessRules) {
        this.businessRules = businessRules;
    }
}
