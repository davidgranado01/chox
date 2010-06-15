package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.util.CalcHelper;
import idas.chox.service.bre.util.InvoiceCalcHelper;

/**
 *
 * @author John
 */
public class HasCorrectTotalLossGrossCalculation implements IBusinessRule {
    private String narrative = "Total Loss Fee Gross calculation is incorrect.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if (claim.getBreBand().isHasCorrectTotalLossGrossCalculation()) {

            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
            boolean success = CalcHelper.EqualTo(claim.getInvoice().getTotalLossFeeGross(), iCalc.getCalculatedTotalLossGross());

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
            }else{
                narrative = "Total Loss Fee Gross calculation is incorrect.";
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
        return "029";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }

}
