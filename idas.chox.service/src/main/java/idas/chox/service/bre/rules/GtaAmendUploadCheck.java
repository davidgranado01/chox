package idas.chox.service.bre.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class GtaAmendUploadCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isUpload414Check() && !ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() && claim.getVehicleHire() != null) {

            boolean success = true;
            Date firstJuly2019 = new Date();
            try {
                firstJuly2019 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2019-06-30 23:59:59.999");
            } catch (ParseException ex) {
                ; // Not reached
            }

            if (claim.getVehicleHire().getHireStart().after(firstJuly2019)
                    && (claim.getHireMonitoringDetail() == null || claim.getHireMonitoringDetail().getEngineersReportSentDate() == null || claim.getHireMonitoringDetail().getWhoIsSendingPav() == null)) {
                success = false;
                narrative = "Correct Managing Repair information in support of GTA 4.14 not provided.";
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
        return "100";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
