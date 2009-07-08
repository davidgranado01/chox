/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.IInvoiceInfo;

/**
 *
 * @author Derm
 * 
 * rule 18, order 8
 */
public class HandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero implements IBusinessRule {

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        IInvoiceInfo invoice = claim.getInvoice();
        
        BigDecimal sum = invoice.getClaimsHandlingInvoiceAmount().add(invoice.getDeductionForClaimsHandlingFee());

        boolean success = CalcHelper.EqualTo(sum, BigDecimal.ZERO);        
        
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        
        return res;

    } 

    public String getNarrative() {
        return "The sum of Claims Handling Invoice Amount and Less Claims Handling Fee does not equate to 0.";
    }

    public String getRuleId() {
        return "018";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }

}
