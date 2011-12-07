package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Invoice;
import idas.chox.service.bre.util.CalcHelper;
import idas.chox.service.bre.util.InvoiceCalcHelper;
import java.math.BigDecimal;


/**
 *
 * @author John
 */
public class HireVatLimitCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(HireVatLimitCheck.class);
    private String narrative = "The CHO is charging more than [current VAT rate] VAT for the Hire.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (claim.getBreBand().isHireVatLimitCheck()) {

            Invoice invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);

            BigDecimal actual = invoice.getHireVat();
            BigDecimal expected = iCalc.getCalculatedHireVat();
            if (claim.getBreBand().getHireVatTolerance() != null)
                expected = expected.add(claim.getBreBand().getHireVatTolerance());
//            boolean success = CalcHelper.LessThanOrEqualTo(actual, expected);
            boolean success = actual.compareTo(expected) <= 0;
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
            }else{
                LOG.debug("Rule failed: Expected={}, actual={}", expected, actual);
                narrative = "The CHO is charging more than " + CalcHelper.VAT_RATE.multiply(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "% VAT for the Hire.";
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
        return "052";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
