package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.*;
import scsbre.model.*;

public class HasCalculatedCorrectDailyRate implements IBusinessRule {

    String narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        RuleEvaluation res = new RuleEvaluation();
        
        if(claim.getVClass() != null){
            
            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

            // Mantis id: 630
            // Change to read vehicleHire's Vehicle Class
            // IVehicleClassInfo customerVClass = claim.getVClass();
            IVehicleClassInfo vehickeHireVClass = claim.getHireDetail().getVClass();
            // System.out.println("BRE CHECK ******************** ");

            /*
            System.out.println("BRE VEHICLE CLASS CODE: "+vehickeHireVClass.getCode());
            System.out.println("BRE VEHICLE CLASS PRICE: "+vehickeHireVClass.getPrice());
            System.out.println("getHireRateChargeTolerance: "+claim.getChoBand().getHireRateChargeTolerance());
            */
            
            // TODO:BUGs
            BigDecimal allowedDailyRate = new BigDecimal(0.00);
            allowedDailyRate = vehickeHireVClass.getPrice().add(claim.getChoBand().getHireRateChargeTolerance());

            // LESS THAN OR EQUAL TO THE TRUE
            // boolean success = cCalc.getDailyHireRateChargedWithToleranceDeduction().compareTo(customerVClass.getPrice()) <= 0;
            boolean success = cCalc.getDailyHireRateCharged().compareTo(allowedDailyRate) <= 0;
            
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);    
            if(success)narrative = "";
            
        } else {
            
            narrative = "Vehicle Hire vehicle class is not specified.";
            res.setResult(RuleEvaluationResult.RuleSkipped);
        }
        
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        return res;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "002";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }    


}
