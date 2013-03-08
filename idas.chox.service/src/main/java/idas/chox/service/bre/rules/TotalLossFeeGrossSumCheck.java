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
import idas.chox.core.util.CalcHelper;
import idas.chox.service.bre.util.InvoiceCalcHelper;

/**
 *
 * @author John
 */
public class TotalLossFeeGrossSumCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(TotalLossFeeGrossSumCheck.class);
    private String narrative = "Total Loss Fee Gross - The sum of the Total Loss Fee Net and the Total Loss Fee VAT is incorrect.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isHasTotalLossFeeGrossSumCheck()) {
            Invoice invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);
            boolean success = CalcHelper.EqualTo(invoice.getTotalLossFeeGross(), invoice.getTotalLossFeeNet().add(invoice.getTotalLossFeeVat()));
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (success) {
                narrative = "";
            } else {
                narrative = "Total Loss Fee Gross - The sum of the Total Loss Fee Net and the Total Loss Fee VAT is incorrect.";
                LOG.debug("Rule failed: total loss fee gross = {}, net + vat = {}",
                        invoice.getTotalLossFeeGross(),
                        invoice.getTotalLossFeeNet().add(invoice.getTotalLossFeeVat()));
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

    @Override
    public String getRuleId() {
        return "051";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
