/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.ClaimCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

/**
 *
 * @author Derm
 * 
 * rule 6, 
 * order 15.
 */
public class ActualHireDaysDoesNotExceedAllowableHireDays implements IBusinessRule {

    
    
    private String narrative = "Number of hire days billed exceeds the allowable threshold (for repair hires).";
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        
        

        
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        

        if (!claim.getHireDetail().getIsTotalLoss() && claim.getEngineeringReport().getEstimatedDaysUnderRepair() > 0) {

            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
            boolean success = claim.getHireDetail().getNumberOfHireDays() <= cCalc.getAllowedDays();
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
            if (success) {
                narrative = "";
            }
        } else {
            
           res.setResult(RuleEvaluationResult.RuleSkipped);
           narrative = "Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair";
        }



        
       
        
        

        
        return res;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "006";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }
    

}
