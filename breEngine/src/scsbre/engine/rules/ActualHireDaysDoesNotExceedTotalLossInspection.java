/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CHOBandCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;


/**
 *
 * @author Derm
 * 
 * rule 7, order 16
 */
public class ActualHireDaysDoesNotExceedTotalLossInspection implements IBusinessRule {

    
    String narrative = "Number of hire days billed exceeds the allowable threshold (for total loss hires).";
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        if(claim.getHireDetail().getIsTotalLoss()){
            
            CHOBandCalcHelper bandCalc = CHOBandCalcHelper.getInstance(claim.getChoBand());
            boolean success =  claim.getHireDetail().getNumberOfHireDays() <= bandCalc.getTotalLossInspectionDays(); 
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
            if(success) narrative = "";
        }
        else{
            res.setResult(RuleEvaluationResult.RuleSkipped);
            narrative = "Rule only applies when the clam is a total loss";
        }


        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        return res;
    }

    public String getNarrative() {
        return narrative;

    }

    public String getRuleId() {
        return "007";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }

}
