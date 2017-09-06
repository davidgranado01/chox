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

public class SubscriberCheckRejectedClaims implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriberCheckRejectedClaims.class);
    private ClaimService claimService;
    private String narrative = "";

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            res.setIsVisibleToCHO(true);
        } else {
            res.setIsVisibleToCHO(false);
        }
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        boolean success = true;

        if (ClaimType.isSubscriber(claim.getClaimType())
               && claim.getBreBand().isSubscriberCheckRejectedClaims() && claim.getVehicleHire() != null) {

            LOG.debug("SubscriberCheckRejectedClaims is activated");

            if (claimService.isSubscriberClaimRejectedAndAgreed(claim)) {
                int hireDays = claim.getVehicleHire().getDays();
                int numDays = claimService.getSubscriberClaimRejectedDays(claim.getId());

                if (hireDays > numDays) {
                    success = false;
                    narrative = "The cumulative number of days prior to the claim rejection was " + numDays
                            + ", allowing the CHO to charge for " + numDays
                            + " hire days, however the CHO are charging for " + hireDays + " hire days.";
                }
                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
            } else {
                LOG.debug("Subscriber claim was not rejected and agreed - rule skipped");
                narrative = "";
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);
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
        return "071";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isSubscriber(claimType)) {
            return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
