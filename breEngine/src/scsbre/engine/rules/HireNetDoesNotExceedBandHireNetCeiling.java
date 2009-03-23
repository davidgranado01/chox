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
 * rule 3, order 13
 * 
 */
public class HireNetDoesNotExceedBandHireNetCeiling implements IBusinessRule {


    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        boolean success = claim.getInvoice().getHireNet().compareTo(claim.getChoBand().getHireNetCeiling()) <= 0;
        
        RuleEvaluation res = new RuleEvaluation();
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        return res;

    }       

    public String getNarrative() {
        return "Hire Net billed exceeds the CHO's Hire Net ceiling.";
    }

    public String getRuleId() {
        return "003";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }    
    
    

}
