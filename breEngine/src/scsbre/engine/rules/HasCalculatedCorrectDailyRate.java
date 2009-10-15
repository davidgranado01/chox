package scsbre.engine.rules;

import java.math.BigDecimal;
import java.util.Locale;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.*;
import scsbre.model.*;

public class HasCalculatedCorrectDailyRate implements IBusinessRule {

    String narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        if(claim.getChoBand().isHasCalculatedCorrectDailyRate()){
            
            if(claim.getVClass() != null){

                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

                // Mantis id: 630
                // Change to read vehicleHire's Vehicle Class
                // IVehicleClassInfo customerVClass = claim.getVClass();
                IVehicleClassInfo vehickeHireVClass = claim.getHireDetail().getVClass();
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

        }else{

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }
        
        return res;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "002";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        // CARLSON @ 20091012
        // HasCalculatedCorrectDailyRate().applyToClaim(claim)) STATUS = InvoiceEscalatedToHandler;
        // return ClaimStatus.InvoiceEscalated;
        return ClaimStatus.InvoiceEscalatedToHandler;
    }    


}
