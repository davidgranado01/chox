package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

/**
 *  Where switched on, the rule shall be applied when the following conditions are met;
 * ‘Total Loss’ is set to ‘No’ and
 *  Claim type is a GTA Claim types, including manual types (i.e. not for Collaboration, Subscriber or Fixed-Fee claims).
 *
 *  The rule shall fail when one or more of the following fields is left blank
 *  Inspection Booked Date
 *  Inspection Date
 *  Repair Book In Date
 *  Date Repair Authorised
 *  Date Repair Commenced
 *  Repair Completion Date
 *
 */
public class RepairDiaryInformationCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isRepairDiaryInfoCheck()) {
            //Specify when rule applies
            if (    claim.getBreBand().isRepairDiaryInfoCheck()
                    && ClaimType.isGTA(claim.getClaimType())
                    && !claim.getHireMonitoringDetail().isIsTotalLostCheck()  ) {

                boolean success = true;

                //Specify when rule fails
                if (   claim.getHireMonitoringDetail().getInspectionBookedDate() == null
                    || claim.getHireMonitoringDetail().getInspectionDate() == null
                    || claim.getHireMonitoringDetail().getRepairBookInDate() == null
                    || claim.getHireMonitoringDetail().getRepairAuthorisedDate() == null
                    || claim.getHireMonitoringDetail().getRepairCommencedDate() == null
                    || claim.getHireMonitoringDetail().getRepairCompletionDate() == null) {
                    success = false;
                    narrative = "The CHO has not presented the diary information as required for hire involving a repair.";
                }

                    res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            } else {

                narrative = "";
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);

            }
        }
        return res;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "108";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isInsurerUpload(claimType)) {
            return ClaimStatus.MANUAL_INVOICE_REJECTED;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}

