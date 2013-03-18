package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.service.bre.util.CHOBandCalcHelper;

public class ActualHireDaysDoesNotExceedTotalLossInspection implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(ActualHireDaysDoesNotExceedTotalLossInspection.class);

    String narrative = "Number of hire days billed by the CHO exceeds the allowable days threshold for total loss hires.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'ActualHireDaysDoesNotExceedTotalLossInspection' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isActualHireDaysDoesNotExceedTotalLossInspection() && claim.getVehicleHire() != null) {

            if (claim.getVehicleHire().getIsTotalLoss()) {
                LOG.debug("Total loss claim - rule applies, hire days = ", claim.getVehicleHire().getDays());
                CHOBandCalcHelper bandCalc = CHOBandCalcHelper.getInstance(claim.getBreBand());
                boolean success = claim.getVehicleHire().getDays() <= bandCalc.getTotalLossInspectionDays();
                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
                if (success) {
                    narrative = "";
                }else{
                    narrative = "The number of hire days billed by the CHO (" + claim.getVehicleHire().getDays() +" days) exceeds the allowable days threshold (" + bandCalc.getTotalLossInspectionDays() + " days) for total loss hires.";
                }

            } else {
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                narrative = "Rule only applies when the claim is a total loss";
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
        return "007";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
