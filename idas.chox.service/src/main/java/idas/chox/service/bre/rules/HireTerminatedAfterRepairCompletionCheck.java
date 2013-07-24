package idas.chox.service.bre.rules;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.DateHelper;

/*
 * John
 */
public class HireTerminatedAfterRepairCompletionCheck implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(HireTerminatedAfterRepairCompletionCheck.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType())  && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isHireTerminatedAfterRepairCompletionCheck()
                && claim.getHireMonitoringDetail() != null && claim.getHireMonitoringDetail().getRepairCompletionDate() != null
                && claim.getVehicleHire() != null && claim.getVehicleHire().getHireEnd() != null) {

            LOG.debug("'Hire Terminated 1 Day After Repairs Were Complete Check' is active");
            Date repairCompletion = claim.getHireMonitoringDetail().getRepairCompletionDate();
            Date hireEnd = claim.getVehicleHire().getHireEnd();
            boolean success = true;
            int noDays = DateHelper.getNumberOfDaysBetween(repairCompletion, hireEnd);

            if (noDays > 1) {
                success = false;
                narrative = "The hire was terminated " + noDays +" days after the repairs were completed (allowed 1 day), please review.";
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
        return "056";
    }


    @Override
    public String getStatusAfterFailure(ClaimType claimType) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}
