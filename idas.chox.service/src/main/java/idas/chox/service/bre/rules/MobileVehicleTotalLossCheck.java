package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class MobileVehicleTotalLossCheck implements IBusinessRule {

    private String narrative = "The CHO's Customer's vehicle was deemed usable/driveable and the claim has been flagged as a Total Loss, please review.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (!ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isMobileVehicleTotalLossCheck()) {
            /*
             * If the CHO's Customer's vehicle has been deemed driveable/usable/mobile
             * and the claim has been flagged as a Total Loss, the invoice will be
             * flagged for review.
             */

            boolean success = !(claim.getCustomer().getIsTotalLoss() && claim.getCustomer().getIsUsable());

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
            }else{
                narrative = "The CHO's Customer's vehicle was deemed usable/driveable and the claim has been flagged as a Total Loss, please review.";
            }

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
        return "070";
    }


    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
