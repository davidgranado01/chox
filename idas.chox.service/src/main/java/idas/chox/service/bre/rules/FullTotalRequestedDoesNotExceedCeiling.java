package idas.chox.service.bre.rules;

import java.text.DecimalFormat;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class FullTotalRequestedDoesNotExceedCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Full Total Requested billed %s exceeds the Full Total Requested ceiling of %s";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

//        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
//                && claim.getBreBand().isFullTotalRequestedCeilingCheck()) {
        if (claim.getBreBand().isFullTotalRequestedCeilingCheck()) {

            boolean success = claim.getInvoice().getFullTotalToPay().compareTo(claim.getBreBand().getFullTotalRequestedCeilingTolerance()) <= 0;
            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (!success) {

                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(claim.getInvoice().getFullTotalToPay().doubleValue()),
                        moneyFormat.format(claim.getBreBand().getFullTotalRequestedCeilingTolerance().doubleValue()));
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
        return "095";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
