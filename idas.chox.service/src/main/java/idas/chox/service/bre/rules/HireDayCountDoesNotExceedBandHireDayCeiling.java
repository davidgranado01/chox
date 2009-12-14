/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

/**
 *
 * @author Derm
 * 
 * rule 4, order 14
 * 
 */
public class HireDayCountDoesNotExceedBandHireDayCeiling implements IBusinessRule {

    private String narrative = "Number of hire days billed by the CHO exceeds the CHO's hire days ceiling.";

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isHireDayCountDoesNotExceedBandHireDayCeiling()) {

            boolean success = claim.getVehicleHire().getDays() <= claim.getBreBand().getHireDayCeiling();
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if (success) {
                narrative = "";
            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }

        return res;

    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "004";
    }

    public String getStatusAfterFailure() {
        // CARLSON @ 20091012
        // return ClaimStatus.INVOICE_ESCALATED;
        // HireDayCountDoesNotExceedBandHireDayCeiling().applyToClaim(claim)) STATUS = InvoiceEscalatedToHandler;
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
