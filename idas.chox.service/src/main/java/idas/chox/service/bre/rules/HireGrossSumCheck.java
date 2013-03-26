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
public class HireGrossSumCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(HireGrossSumCheck.class);
    private String narrative = "Hire Gross - The sum of the Hire Net and the Hire VAT is incorrect.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule HireGrossSumCheck");
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        if (claim.getBreBand().isHasHireGrossSumCheck()) {
            Invoice invoice = claim.getInvoice();
            boolean success = CalcHelper.equalTo(invoice.getHireGross(), invoice.getHireNet().add(invoice.getHireVat()));
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (success) {
                narrative = "";
                LOG.debug("Rule HireGrossSumCheck passed.");
            } else {
                narrative = "Hire Gross - The sum of the Hire Net and the Hire VAT is incorrect.";
                LOG.debug("Rule failed: hire gross = {}, net + vat = {}",
                        invoice.getHireGross(),
                        invoice.getHireNet().add(invoice.getHireVat()));
            }
        } else {
            narrative = "";
            LOG.debug("Skipping rule HireGrossSumCheck");
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
        return "048";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
