/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class LabourCostBusinessRule implements IBusinessRule {

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        boolean success = true;
        
        RuleEvaluation res = new RuleEvaluation();
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        
        if(isRequiredToValidateByBRE(claim)){
            
            // System.out.println("START BRE - 22 - LabourCostBusinessRule");
            
            int iNumberOfHireDay =claim.getHireDetail().getNumberOfHireDays();
            int iNumberDayOfLabourCostWorthy = cCalc.getNumberDayOfLabourCostWorthy();
            
            // System.out.println("iNumberOfHireDay:"+iNumberOfHireDay);
            // System.out.println("iNumberDayOfLabourCostWorthy:"+iNumberDayOfLabourCostWorthy);
            
            if(iNumberOfHireDay>=iNumberDayOfLabourCostWorthy){
                success = false;
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
            
        }else{
            
            res.setResult(RuleEvaluationResult.RuleSkipped);
            
        }
        
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        return res;
    }
    
    private boolean isRequiredToValidateByBRE(IClaimInfo claim){
        
        boolean bFlag = true;

        if(claim.getHireMonitoringDetail()!=null){
            
            BigDecimal bLabourCost = new BigDecimal(0.00);
            int iLabourHour = 0;
        
            if(claim.getHireMonitoringDetail().getLabourCost()!=null){
                bLabourCost = claim.getHireMonitoringDetail().getLabourCost();
            }
            
            if(claim.getHireMonitoringDetail().getLabourHour()!=null){
                iLabourHour = claim.getHireMonitoringDetail().getLabourHour();
            }
            
            if((bLabourCost.compareTo(new BigDecimal(0.00))<1) && (iLabourHour<=0)){
                bFlag = false;
            }       
            
        }else{
            bFlag = false;
        }
 
        return bFlag;
    }
    
    public String getNarrative() {
        return "Insufficient information to perform labour cost rule";
    }

    public String getRuleId() {
        return "022";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }

}
