package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

/**
 *
 * @author Derm
 * 
 * rule 4, order 14
 * 
 */
public class HireDayCountDoesNotExceedBandHireDayCeiling implements IBusinessRule {

    private String narrative = "Number of hire days billed by the CHO exceeds the CHO's hire days ceiling.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (claim.getBreBand().isHireDayCountDoesNotExceedBandHireDayCeiling() && claim.getVehicleHire() != null) {

            boolean success = claim.getVehicleHire().getDays() <= claim.getBreBand().getHireDayCeiling();
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
            }else{
//                narrative = "Number of hire days billed by the CHO exceeds the CHO's hire days ceiling.";
                narrative = "The number of hire days billed by the CHO (" + claim.getVehicleHire().getDays() + " days) exceeds the CHO's hire days ceiling (" + claim.getBreBand().getHireDayCeiling() + " days).";
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
        return "004";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        // CARLSON @ 20091012
        // return ClaimStatus.INVOICE_ESCALATED;
        // HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim)) STATUS = InvoiceEscalatedToHandler;
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
