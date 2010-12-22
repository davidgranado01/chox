package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Invoice;
import idas.chox.service.bre.util.CalcHelper;
import idas.chox.service.bre.util.InvoiceCalcHelper;
import java.math.BigDecimal;

/**
 *
 * @author John
 */
public class TotalLossFeeVatLimitCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(TotalLossFeeVatLimitCheck.class);
    private String narrative = "Total Loss Fee VAT charged is too high.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if (claim.getBreBand().isTotalLossFeeVatLimitCheck()) {

            Invoice invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);

            BigDecimal actual = invoice.getTotalLossFeeVat();
            BigDecimal expected = iCalc.getCalculatedTotalLossVat();

//            boolean success = CalcHelper.LessThanOrEqualTo(actual, expected);
            boolean success = actual.compareTo(expected) <= 0;
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
            }else{
                narrative = "The CHO is charging more than " + CalcHelper.VAT_RATE.multiply(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "% VAT for the Total Loss Fee.";
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

    public boolean isVisibleToCHO() {
        return true;
    }

    @Override
    public String getRuleId() {
        return "054";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
