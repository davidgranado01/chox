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

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        
        
        ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
        boolean success = claim.getHireDetail().getNumberOfHireDays() <= cCalc.getAllowedDays();
        
        RuleEvaluation res = new RuleEvaluation();
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        
        return res;
    }

    public String getNarrative() {
        return "Number of hire days billed exceeds the allowable threshold (for repair hires).";
    }

    public String getRuleId() {
        return "006";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }
    

}
