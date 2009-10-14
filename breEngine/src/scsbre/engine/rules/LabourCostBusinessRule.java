package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class LabourCostBusinessRule implements IBusinessRule {
    
    private String narrative = "";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        boolean success = true;
        
        RuleEvaluation res = new RuleEvaluation();
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        
        if(isRequiredToValidateByBRE(claim)){
            
            int iNumberOfHireDay =claim.getHireDetail().getNumberOfHireDays();
            int iNumberDayOfLabourCostWorthy = cCalc.getNumberDayOfLabourCostWorthy();
            
            if(iNumberOfHireDay>iNumberDayOfLabourCostWorthy){
                success = false;
                narrative = "The number of hire days billed by the CHO is not relative to the number of expected hire days based on the labour information provided.";
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
            
        }else{
            
            res.setResult(RuleEvaluationResult.RuleSkipped);
            narrative = "Insufficient information to perform labour cost rule.";
            
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
        return narrative;
    }

    public String getRuleId() {
        return "022";
    }

    public ClaimStatus getStatusAfterFailure() {
        // CARLSON @ 20091012
        // LabourCostBusinessRule().applyToClaim(claim)) STATUS = InvoiceEscalatedToHandler;
        // return ClaimStatus.InvoiceEscalated;
        return ClaimStatus.InvoiceEscalatedToHandler;
    }

}
