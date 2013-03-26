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
        res.setClaimType(claim.getClaimType());
        if (claim.getBreBand().isHasRepairGrossSumCheck()) {
            Invoice invoice = claim.getInvoice();
            boolean success = CalcHelper.equalTo(invoice.getRepairGross(), invoice.getRepairNet().add(invoice.getRepairVat()));
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (success) {
                narrative = "";
            } else {
                narrative = "Repair Gross - The sum of the Repair Net and the Repair VAT is incorrect.";
                LOG.debug("Rule failed: repair gross = {}, net + vat = {}",
                        invoice.getRepairGross(),
                        invoice.getRepairNet().add(invoice.getRepairVat()));
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
        return "047";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
