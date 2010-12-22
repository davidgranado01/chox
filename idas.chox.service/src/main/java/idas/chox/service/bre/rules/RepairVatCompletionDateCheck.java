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
public class RepairVatCompletionDateCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(RepairVatCompletionDateCheck.class);
    private String narrative = "The CHO is charging more than [current VAT rate] VAT for the Repair.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if (claim.getBreBand().isRepairVatCompletionDateCheck()) {

            Invoice invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);

            BigDecimal actual = invoice.getRepairVat();
            BigDecimal expected = iCalc.getCalculatedRepairVat(claim.getHireMonitoringDetail().getRepairCompletionDate());
            LOG.debug("Actual VAT={}, expected={}", actual, expected);
//            boolean success = CalcHelper.LessThanOrEqualTo(actual, expected);
            boolean success = actual.compareTo(expected) <= 0;
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
                LOG.debug("Rule passed");
            }else{
                narrative = "The CHO is charging more than the Repair-completion VAT rate of " + CalcHelper.getVatRate(claim.getHireMonitoringDetail().getRepairCompletionDate()).multiply(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_DOWN) + "% for the Repair.";
                LOG.debug("Rule failed: {}", narrative);
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
        return "060";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
