/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Invoice;
import idas.chox.service.bre.util.CalcHelper;
import java.math.BigDecimal;

/**
 *
 * @author Derm
 * 
 * rule 18, order 8
 */
public class HandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero implements IBusinessRule {

    String narrative = "The sum of Claims Handling Invoice Amount and Less Claims Handling Fee does not equate to 0.";

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if (claim.getBreBand().isHandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero()) {

            Invoice invoice = claim.getInvoice();
            BigDecimal sum = invoice.getClaimsHandlingInvoiceAmount().add(invoice.getDeductionForClaimsHandlingFee());
            boolean success = CalcHelper.EqualTo(sum, BigDecimal.ZERO);
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }

        return res;

    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "018";
    }

    public String getStatusAfterFailure() {
        // HandlingInvoiceAmountAddedToDeductionForHandlingFeeEqualsZero().applyToClaim(claim)) STATUS = InvoiceDataCalculationIncorrect;
        // CARLSON @ 20091012
        // return ClaimStatus.INVOICE_ESCALATED;
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
