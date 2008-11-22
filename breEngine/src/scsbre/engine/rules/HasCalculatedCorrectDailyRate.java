/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


//rule 2, order 2



package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.*;
import scsbre.model.*;


/**
 *
 * @author Derm
 */
public class HasCalculatedCorrectDailyRate implements IBusinessRule {

    String narrative = "Daily rate billed for replacement vehicle class exceeds ABI rate.";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        RuleEvaluation res = new RuleEvaluation();
        if(claim.getVClass() != null){
            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
            IVehicleClassInfo customerVClass = claim.getVClass();
            boolean success = cCalc.getDailyHireRateChargedWithToleranceDeduction().compareTo(customerVClass.getPrice()) <= 0;
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);    
            if(success)narrative = "";
        }
        else{
            narrative = "Customer vehicle class is not specified.";
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
        return ClaimStatus.AwaitingPaymentPack;
    }    


}
