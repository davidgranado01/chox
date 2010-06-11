package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.math.BigDecimal;

/**
 *
 * @author John
 */
public class AdditionalDriverChargeCheck implements IBusinessRule {
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        boolean success = true;

        if (claim.getBreBand().isAdditionalDriverChargeCheck()) {

            if (claim.getInvoice().getAutomaticFee().compareTo(new BigDecimal(0)) > 0) {
                success = false;
                narrative = "The CHO is charging an additional driver fee for the hire, please review need.";
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

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
        return "028";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

}
