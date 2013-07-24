package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class BabySeatChargeCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        if (ClaimType.isSubscriber(claim.getClaimType()) || ClaimType.isCollaborationProtocol(claim.getClaimType())) {
            res.setIsVisibleToCHO(true);
        } else {
            res.setIsVisibleToCHO(false);
        }
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isBabySeatChargeCheck()) {

            boolean success = true;

            if (claim.getInvoice().getBabySeatFee().compareTo(BigDecimal.ZERO) > 0) {
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
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isSubscriber(claimType) || ClaimType.isCollaborationProtocol(claimType)) {
            return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}

