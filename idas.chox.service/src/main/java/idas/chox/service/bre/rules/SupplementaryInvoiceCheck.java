package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.ClaimService;

/**
 *
 * @author seeni
 */
public class SupplementaryInvoiceCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(SupplementaryInvoiceCheck.class);
    private String narrative;
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());
        LOG.debug("Applying rule 'SupplementaryInvoiceCheck' to claim {}.", claim.getChoReference());

        if (ClaimType.isSupplementaryInvoice(claim.getClaimType())
            && !ClaimType.isOriginalSupplementaryInvoice(claim.getClaimType())) {
            res.setResult(RuleEvaluationResult.RULE_FAILED);
            Claim originalSuppInv = claimService.getOriginalSupplementaryInvoicedClaim(claim.getCustomer().getClaimReference());
            if (originalSuppInv != null) {
                String originalSuppInvChoRef = originalSuppInv.getChoReference();
                narrative = "This is a supplementary Invoice. The original claim's supplier reference is " + originalSuppInvChoRef + ".";
                LOG.debug("Rule failed: {}", narrative);
            } else {
                narrative = "This is a supplementary Invoice.";
                LOG.error("Rule failed {} and could not find original Supplementary Claim for supplementary Invoice: {}", narrative,claim.getChoReference());
            }

        } else {
            LOG.debug("Rule not switched on.");
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
        return "999";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
