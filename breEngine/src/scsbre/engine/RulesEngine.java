package scsbre.engine;

import scsbre.engine.rules.*;
import scsbre.model.IClaimInfo;

public class RulesEngine {

    private IClaimInfo claim;

    public static RulesEngine getInstance(IClaimInfo c) {
        RulesEngine engine = new RulesEngine(c);
        return engine;
    }

    private RulesEngine(IClaimInfo c) {
        claim = c;
    }

    public RulesEngineResponse ResolveStatus() {
        RulesEngineResponse response = new RulesEngineResponse();
        response.addRuleEvaulation(new HasAllowedVehicleClass().applyToClaim(claim));
        response.addRuleEvaulation(new HasCalculatedCorrectDailyRate().applyToClaim(claim));
        response.addRuleEvaulation(new HireNetDoesNotExceedVehicleClassHireNetCeiling().applyToClaim(claim));
        response.addRuleEvaulation(new HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim));
        response.addRuleEvaulation(new HasCorrectHireGrossCalculation().applyToClaim(claim));
        response.addRuleEvaulation(new ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim));
        response.addRuleEvaulation(new ActualHireDaysDoesNotExceedTotalLossInspection().applyToClaim(claim));
        response.addRuleEvaulation(new RepairGrossIsLessThanEstimatedTotalRepairAmount().applyToClaim(claim));
        response.addRuleEvaulation(new HasCorrectHireVatCalculation().applyToClaim(claim));
        response.addRuleEvaulation(new HasCorrectRepairVatCalculation().applyToClaim(claim));
        response.addRuleEvaulation(new HasCorrectRepairGrossCalculation().applyToClaim(claim));
        response.addRuleEvaulation(new HasCorrectTotalNet().applyToClaim(claim));
        response.addRuleEvaulation(new HasCorrectTotalVat().applyToClaim(claim));   
        response.addRuleEvaulation(new HasCalculatedTotalGrossEqualSuppliedTotalGross().applyToClaim(claim)); 
        response.addRuleEvaulation(new HasCorrectDiscountForNonDA().applyToClaim(claim)); 
        response.addRuleEvaulation(new HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim)); 
        response.addRuleEvaulation(new ClaimHasZeroDiscountForDA().applyToClaim(claim));  
        response.addRuleEvaulation(new HandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero().applyToClaim(claim));    
        response.addRuleEvaulation(new HasSuppliedCorrectTotalToPay().applyToClaim(claim));        
        response.addRuleEvaulation(new EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays().applyToClaim(claim)); 
        response.addRuleEvaulation(new LabourCostBusinessRule().applyToClaim(claim)); 
        response.addRuleEvaulation(new RepairNetDoesNotExceedVehicleClassRepairNetCeiling().applyToClaim(claim)); //Emmanuel 18-09-2009
        response.addRuleEvaulation(new NumberOfHireDaysReconcile().applyToClaim(claim));            // RULE 024
        response.addRuleEvaulation(new CorrentAdminFee().applyToClaim(claim));                      // RULE 025
        response.addRuleEvaulation(new RepairBookedInDateOnFriday().applyToClaim(claim));           // RULE 026
        response.addRuleEvaulation(new FlaggedForManualInvoiceReview().applyToClaim(claim));        // RULE 027
        response.addRuleEvaulation(new AutomaticChargeCheck().applyToClaim(claim));                 // RULE 028
        response.addRuleEvaulation(new EstateChargeCheck().applyToClaim(claim));                    // RULE 029
        response.addRuleEvaulation(new NonStandardRiskInsurancePremiumCheck().applyToClaim(claim)); // RULE 030
        response.addRuleEvaulation(new CDWChargeCheck().applyToClaim(claim));                       // RULE 031
        response.addRuleEvaulation(new SatelliteNavigationChargeCheck().applyToClaim(claim));       // RULE 032
        response.addRuleEvaulation(new BabySeatChargeCheck().applyToClaim(claim));                  // RULE 033
        response.addRuleEvaulation(new TowBarsChargeCheck().applyToClaim(claim));                   // RULE 034
        response.addRuleEvaulation(new RoofRackChargeCheck().applyToClaim(claim));                  // RULE 035
        response.addRuleEvaulation(new DeliveryOrCollectionChargeCheck().applyToClaim(claim));      // RULE 036
        response.addRuleEvaulation(new DualControlChargeCheck().applyToClaim(claim));               // RULE 037
        response.addRuleEvaulation(new RepairBookedInDateOnSaturday().applyToClaim(claim));         // RULE 038
        response.addRuleEvaulation(new RepairBookedInDateOnSunday().applyToClaim(claim));           // RULE 039
        response.addRuleEvaulation(new HireNetDoesNotExceedHireNetCeiling().applyToClaim(claim));       // RULE 040
        response.addRuleEvaulation(new RepairNetDoesNotExceedRepairNetCeiling().applyToClaim(claim));   // RULE 041
        response.addRuleEvaulation(new validateUniqueVehicleRegistrationNumber().applyToClaim(claim));
        return response;
    }   
}