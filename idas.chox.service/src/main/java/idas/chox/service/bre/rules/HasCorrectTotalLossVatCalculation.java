package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Invoice;
import idas.chox.core.util.CalcHelper;
import idas.chox.service.bre.util.InvoiceCalcHelper;

/**
 *
 * @author John
 */
public class HasCorrectTotalLossVatCalculation implements IBusinessRule {
    private String narrative = "Total Loss Fee VAT calculation is incorrect.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isHasCorrectTotalLossVatCalculation()) {

            Invoice invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);

            BigDecimal actual = invoice.getTotalLossFeeVat();
            BigDecimal expected = iCalc.getCalculatedTotalLossVat();

            boolean success = CalcHelper.equalTo(actual, expected);
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (success) {
                narrative = "";
            }else{
                narrative = "Total Loss Fee VAT calculation is incorrect.";
            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        return res;

    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    public boolean isVisibleToCHO() {
        return true;
    }

    @Override
    public String getRuleId() {
        return "030";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }

}
