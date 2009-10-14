/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

/**
 *
 * @author Derm
 * 
 * rule 4, order 14
 * 
 */
public class HireDayCountDoesNotExceedBandHireDayCeiling implements IBusinessRule{

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        

        boolean success = claim.getHireDetail().getNumberOfHireDays() <= claim.getChoBand().getHireDayCeiling();
        
        RuleEvaluation res = new RuleEvaluation();
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        return res;

    }       

    public String getNarrative() {
        return "Number of hire days billed by the CHO exceeds the CHO's hire days ceiling.";
    }

    public String getRuleId() {
        return "004";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        // CARLSON @ 20091012
        // return ClaimStatus.InvoiceEscalated;
        // HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim)) STATUS = InvoiceEscalatedToHandler;
        return ClaimStatus.InvoiceEscalatedToHandler;
    }

}
