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
        response.addRuleEvaulation(new HireNetDoesNotExceedBandHireNetCeiling().applyToClaim(claim));
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
        
        return response;
    }

    
    
   
}
