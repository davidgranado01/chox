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
import idas.chox.service.bre.util.ClaimCalcHelper;

public class ActualHireDaysDoesNotExceedAllowableHireDays implements IBusinessRule {

    private String narrative = "Number of hire days billed by the CHO exceeds the allowable days threshold (non total loss), taking into account the ECD(s) provided by the CHO and the additional days allowed through delay variables.";
    
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getBreBand().isActualHireDaysDoesNotExceedAllowableHireDays()){

            if (!claim.getVehicleHire().getIsTotalLoss() && claim.getEngineerReport().getEstimatedDaysUnderRepair() < 1) {

                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);
                boolean success = claim.getVehicleHire().getDays() <= cCalc.getAllowedDays();

                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                
                if (success) {
                    narrative = "";
                }

            } else {
               res.setResult(RuleEvaluationResult.RuleSkipped);
               narrative = "Claim is total loss OR a non-zero value has been supplied for Estimated Days Under Repair";
            }

        }else{

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }

        return res;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "006";
    }

    public String getStatusAfterFailure() {
        // CARLSON @ 20091012
        // ActualHireDaysDoesNotExceedAllowableHireDays().applyToClaim(claim)) STATUS = InvoiceEscalatedToHandler;
        // return ClaimStatus.INVOICE_ESCALATED;
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
    

}
