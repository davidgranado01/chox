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

/**
 *
 * @author John
 */
public class RepairGrossSumCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(RepairGrossSumCheck.class);
    private String narrative = "Repair Gross - The sum of the Repair Net and the Repair VAT is incorrect.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        if (claim.getBreBand().isHasRepairGrossSumCheck()) {
            Invoice invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);
            boolean success = CalcHelper.EqualTo(invoice.getRepairGross(), invoice.getRepairNet().add(invoice.getRepairVat()));
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
            } else {
                LOG.debug("Rule failed: repair gross = {}, net + vat = {}",
                        invoice.getRepairGross(),
                        invoice.getRepairNet().add(invoice.getRepairVat()));
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
        return "047";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
