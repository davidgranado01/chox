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

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        CHOBandCalcHelper bandCalc = CHOBandCalcHelper.getInstance(claim.getChoBand());
        boolean success =  claim.getHireDetail().getNumberOfHireDays() <= bandCalc.getTotalLossInspectionDays();
        
        RuleEvaluation res = new RuleEvaluation();
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        return res;
    }

    public String getNarrative() {
        return "Number of hire days billed exceeds the allowable threshold (for total loss hires).";

    }

    public String getRuleId() {
        return "007";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }

}
