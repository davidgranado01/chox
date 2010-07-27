/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Customer;
import idas.chox.core.model.EngineerReport;
import idas.chox.service.bre.util.ClaimCalcHelper;

public class EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays.class);

    private String narrative = "Number of hire days billed exceeds the allowable threshold (non total loss) with the inclusion of the Engineer's Esimtated Days Under Repair.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        LOG.debug("Applying rule 'EstimatedRepairDaysPlusBandDaysDoNotExceedHireDays' to claim {}.", claim.getChoReference());

        if (claim.getBreBand().isEstimatedRepairDaysPlusBandDaysDoNotExceedHireDays()) {

            Customer cvdamage = claim.getCustomer();
            BreBand choBand = claim.getBreBand();
            EngineerReport eReport = claim.getEngineerReport();

            if ((claim.getVehicleHire().getIsTotalLoss()) || (eReport.getEstimatedDaysUnderRepair() < 1)) {

                narrative = "Claim is a Total Loss or Estimated Days Under Repair is less than 1";
                LOG.debug("Rule skipped: isTotalLoss: {}, estimatedDaysUberRepair: {}", claim.getVehicleHire().getIsTotalLoss(), eReport.getEstimatedDaysUnderRepair());
                res.setResult(RuleEvaluationResult.RuleSkipped);

            } else {

                ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

                int hireDays = claim.getVehicleHire().getDays();

                int takeVehicleToGarageDays = cvdamage.getIsUsable()
                        ? choBand.getTakeVehicleToGarageDaysMobile()
                        : choBand.getTakeVehicleToGarageDaysNonMobile();

                int maxDays = eReport.getEstimatedDaysUnderRepair();

                maxDays += takeVehicleToGarageDays;

                // Basecamp : S8019
                // maxDays += choBand.getWeekendBufferDays();
                maxDays += choBand.getTakeVehicleOutDays();
                maxDays += choBand.getEngineerInspectionDelayDays();

                maxDays += cCalc.getWeekendBuffer(maxDays);

                boolean success = hireDays <= maxDays;

                if (success) {
                    narrative = "";
                    LOG.debug("Rule passed.");
                    res.setResult(RuleEvaluationResult.RulePassed);

                } else {
                    LOG.debug("Hire days ({}) > max allowed days ({})", hireDays, maxDays);
//                    narrative = "Number of hire days billed exceeds the allowable threshold (non total loss) with the inclusion of the Engineer's Esimtated Days Under Repair.";
                    narrative = "The number of hire days billed by the CHO (" + hireDays + " days) exceeds the allowable threshold for non total loss hires (" + maxDays + " days) with the inclusion of the Engineer's esimtated days under repair.";
                    res.setResult(RuleEvaluationResult.RuleFailed);

                }

            }

        } else {

            narrative = "";
            LOG.debug("Rule skipped: rule disabled");
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
        return "020";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED;
    }
}
