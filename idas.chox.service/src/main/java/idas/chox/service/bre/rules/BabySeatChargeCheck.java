package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import java.math.BigDecimal;

public class BabySeatChargeCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (claim.getBreBand().isBabySeatChargeCheck()) {

            boolean success = true;

            if (claim.getInvoice().getBabySeatFee().compareTo(new BigDecimal(0)) > 0) {
                success = false;
                narrative = "The CHO is charging a baby seat fee for the hire, please review need.";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

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
        return "033";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}

