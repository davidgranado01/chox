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
public class TotalGrossSumCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(TotalGrossSumCheck.class);
    private String narrative = "Total Gross - The sum of the Total Net and the Total VAT is incorrect.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        if (claim.getBreBand().isHasTotalGrossSumCheck()) {
            Invoice invoice = claim.getInvoice();
            boolean success = CalcHelper.equalTo(invoice.getTotalGross(), invoice.getTotalNet().add(invoice.getTotalVat()));
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (success) {
                narrative = "";
            } else {
                narrative = "Total Gross - The sum of the Total Net and the Total VAT is incorrect.";
                LOG.debug("Rule failed: total gross = {}, net + vat = {}",
                        invoice.getTotalGross(),
                        invoice.getTotalNet().add(invoice.getTotalVat()));
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
        return "050";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
