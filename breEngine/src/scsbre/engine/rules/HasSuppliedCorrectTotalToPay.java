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
 *
 * @author Derm
 * 
 * rule 19, order 8;
 * 
 */
public class HasSuppliedCorrectTotalToPay implements IBusinessRule {

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        
        IInvoiceInfo invoice = claim.getInvoice();
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);
        boolean success = CalcHelper.EqualTo(iCalc.getCalculatedTotalToPay(), invoice.getTotalToPay());
        
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        
        return res;

    }

    public String getNarrative() {
        return "Total to Pay calculation is incorrect.";
    }

    public String getRuleId() {
        return "019";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.AwaitingPaymentPack;
    }
    
    

}
