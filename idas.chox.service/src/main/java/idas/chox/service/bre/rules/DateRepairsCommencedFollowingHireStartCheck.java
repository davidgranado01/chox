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
public class DateRepairsCommencedFollowingHireStartCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isDateRepairCommencedChkForNonMobileVehicle() && !claim.getCustomer().getIsUsable()) {

            boolean success = true;

            int maxDays = claim.getBreBand().getHireDaysPriorToDateRepairCommenced();
            Date repairCommDate = claim.getHireMonitoringDetail().getRepairCommencedDate();
            Date hireStartDate = claim.getVehicleHire().getHireStart();

            int noOfDays = (int) ((repairCommDate.getTime() - hireStartDate.getTime()) / (1000 * 60 * 60 * 24));

            if (noOfDays < maxDays) {

                narrative = "";

            } else {

                success = false;
                narrative = "The hire commenced [" + noOfDays + " days] prior to the date repairs commenced, the allowable number of days is [" + maxDays + " days] for un-driveable vehicles.";
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
        return "064";


    }

    @Override
    public String getStatusAfterFailure() {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}
