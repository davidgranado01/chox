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
import idas.chox.service.bre.util.CalcHelper;
import java.math.BigDecimal;

/**
 *
 * @author Derm rule 17, order 10
 */
public class ClaimHasZeroDiscountForDA implements IBusinessRule {

    String narrative = "CHO is on DA scheme. Discount should be 0.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if (claim.getBreBand().isClaimHasZeroDiscountForDA()) {

            if (claim.getChorganisation().isDelegatedAuthority()) {

                boolean success = CalcHelper.EqualTo(claim.getInvoice().getDiscount(), BigDecimal.ZERO);
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

                if (success) {
                    narrative = "";
                }else{
                    narrative = "CHO is on DA scheme. Discount should be 0.";
                }

            } else {

                narrative = "Rule does not apply to CHOs not in the DA scheme";
                res.setResult(RuleEvaluationResult.RuleSkipped);

            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }

        return res;

    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "017";
    }

    @Override
    public String getStatusAfterFailure() {
        // CARLSON @ 20091012
        // ClaimHasZeroDiscountForDA().applyToClaim(claim)) STATUS = InvoiceDataCalculationIncorrect; 
        //return ClaimStatus.INVOICE_ESCALATED;
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
