package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class RepairVatLimitCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(RepairVatLimitCheck.class);
    private String narrative = "The CHO is charging more than [current VAT rate] VAT for the Repair.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isRepairVatLimitCheck()) {

            Invoice invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);

            BigDecimal actual = invoice.getRepairVat();
            BigDecimal expected = iCalc.getCalculatedRepairVat();
            if (claim.getBreBand().getRepairVatTolerance() != null)
                expected = expected.add(claim.getBreBand().getRepairVatTolerance());
            LOG.debug("Actual VAT={}, expected={}", actual, expected);
//            boolean success = CalcHelper.LessThanOrEqualTo(actual, expected);
            boolean success = actual.compareTo(expected) <= 0;
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (success) {
                narrative = "";
                LOG.debug("Rule passed");
            }else{
                narrative = "The CHO is charging more than " + CalcHelper.VAT_RATE.multiply(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "% VAT for the Repair.";
                LOG.debug("Rule failed: {}", narrative);
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
        return "053";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
