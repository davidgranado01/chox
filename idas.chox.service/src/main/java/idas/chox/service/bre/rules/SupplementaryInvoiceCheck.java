/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

/**
 *
 * @author seeni
 */
public class SupplementaryInvoiceCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(SupplementaryInvoiceCheck.class);
    private String narrative;

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setIsTPIClaim(claim.isTpiClaim());
        LOG.debug("Applying rule 'SupplementaryInvoiceCheck' to claim {}.", claim.getChoReference());

        if (claim.isSupplementaryInvoicedClaim()) {
            res.setResult(RuleEvaluationResult.RuleFailed);
            narrative = claim.getComments().get(0).getComment();
            LOG.debug("Rule failed: {}", narrative);
        } else {
            LOG.debug("Rule not switched on.");
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
        return "999";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
