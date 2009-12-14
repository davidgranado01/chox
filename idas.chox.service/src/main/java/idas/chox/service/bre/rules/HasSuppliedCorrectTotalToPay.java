package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Invoice;
import idas.chox.service.bre.util.CalcHelper;
import idas.chox.service.bre.util.InvoiceCalcHelper;

public class HasSuppliedCorrectTotalToPay implements IBusinessRule {

    private String narrative = "Total to Pay calculation is incorrect.";

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if (claim.getBreBand().isHasSuppliedCorrectTotalToPay()) {

            Invoice invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);
            boolean success = CalcHelper.LessThanOrEqualTo(invoice.getTotalToPay(), iCalc.getCalculatedTotalToPay());
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
        return "019";
    }

    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
