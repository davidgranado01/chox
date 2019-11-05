package idas.chox.service.bre.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

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

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType())
                && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isActualHireDaysDoesNotExceedTotalLossInspection()
                && claim.getCustomer() != null && claim.getVehicleHire() != null && claim.getHireMonitoringDetail() != null) {

            Date firstJuly2019 = new Date();
            try {
                firstJuly2019 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2019-06-30 23:59:59.999");
            } catch (ParseException ex) {
                ; // Not reached
            }
            
            if (claim.getCustomer().getIsTotalLoss() && claim.getVehicleHire().getHireStart().after(firstJuly2019)
                    && (claim.getHireMonitoringDetail().getEngineersReportSentDate() == null || claim.getHireMonitoringDetail().getWhoIsSendingPav() == null)) {
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
