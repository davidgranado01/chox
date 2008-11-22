/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.IInvoiceInfo;

/**
 * rule 9, order 3
 * @author Derm
 */
public class HasCorrectHireVatCalculation implements IBusinessRule {


    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        
        IInvoiceInfo invoice = claim.getInvoice();
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);
        boolean success = CalcHelper.EqualTo(invoice.getHireVat(), iCalc.getCalculatedHireVat());
        
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        

        return res;

    }    
    
    public String getFailureMessage() {
        return "Hire VAT calculation is incorrect.";
    }

    public boolean isVisibleToCHO() {
        return true;
    }

    public boolean appliesToClaim(IClaimInfo claim) {
        return true;
    }

    public String getRuleId() {
        return "009";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.AwaitingPaymentPack;
    }    

}
