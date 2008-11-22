/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.IClaimInfo;
import scsbre.engine.util.CalcHelper;
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.ClaimStatus;
/**
 *
 * @author Derm
 * 
 * rule 5, order 5
 */
public class HasCorrectHireGrossCalculation implements IBusinessRule {

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        boolean success = CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());
        
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        

        return res;

    }

    public String getNarrative() {
        return "Hire Gross calculation is incorrect.";
    }


    public String getRuleId() {
       return "005";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.AwaitingPaymentPack;
    }



}
