package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

/**
 *  Where switched on, the rule shall be applied when the following conditions are met:
 *  ‘Total Loss’ is set to ‘Yes’
 *  Claim type is GTA Claim types, including manual types (i.e. not for Collaboration, Subscriber or Fixed-Fee claims).
 *
 *  The rule shall fail when one or more of the following fields is left blank:
 *  Inspection Booked Date
 *  Inspection Date
 *  Date repair authorised/TL identified
 *  Date engineers report sent
 *  Date Total Loss Offer Made
 *  Date Total Loss Offer Accepted
 *  Date Total Loss Cheque Issued
 *  Date Total Loss Cheque Received
 */


public class TotalLossDiaryInformationCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        //Specify when rule applies
        if (    ClaimType.isGTA(claim.getClaimType())
                && !ClaimType.isCollaborationProtocol(claim.getClaimType())
                && !ClaimType.isSubscriber(claim.getClaimType())
                && !ClaimType.isFixedFee(claim.getClaimType())
                && !claim.getHireMonitoringDetail().isIsTotalLostCheck()  ) {

            boolean success = true;

            //Specify when rule fails
            if (   claim.getHireMonitoringDetail().getInspectionBookedDate() == null
                || claim.getHireMonitoringDetail().getInspectionDate() == null
                || claim.getHireMonitoringDetail().getRepairAuthorisedDate() == null
                || claim.getHireMonitoringDetail().getEngineersReportSentDate() == null
                || claim.getHireMonitoringDetail().getTotalLossOfferMadeDate() == null
                || claim.getHireMonitoringDetail().getTotalLossOfferAcceptedDate() == null
                || claim.getHireMonitoringDetail().getTotalLossOfferCheckIssuedDate() == null
                || claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate() == null) {
                success = false;
                narrative = "The CHO has not presented the diary information as required for hire involving a total loss.";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

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
        return "107";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        if (ClaimType.isInsurerUpload(claimType)) {
            return ClaimStatus.MANUAL_INVOICE_REJECTED;
        }
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}

