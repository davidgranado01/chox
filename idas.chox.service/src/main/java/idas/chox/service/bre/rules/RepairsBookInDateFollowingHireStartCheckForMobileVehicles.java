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
import java.util.Date;

/*
 * Rajareddy Dodda
 */
public class RepairsBookInDateFollowingHireStartCheckForMobileVehicles implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isDateRepairBookInDateChkForMobileVehicle() && claim.getCustomer().getIsUsable()) {

            boolean success = true;

            int maxDays = claim.getBreBand().getHireDaysPriorToDateRepairBookInDateMobileVehicles();
            Date repairBookInDate = claim.getHireMonitoringDetail().getRepairBookInDate();
            Date hireStartDate = claim.getVehicleHire().getHireStart();

            int noOfDays = (int) ((repairBookInDate.getTime() - hireStartDate.getTime()) / (1000 * 60 * 60 * 24));

            if (noOfDays < maxDays) {

                narrative = "";

            } else {

                success = false;
                narrative = "The hire commenced [" + noOfDays + " days] prior to the repair book in date, the allowable number of days is [" + maxDays + " days] for driveable vehicles.";
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
        return "066";


    }

    @Override
    public String getStatusAfterFailure() {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}
