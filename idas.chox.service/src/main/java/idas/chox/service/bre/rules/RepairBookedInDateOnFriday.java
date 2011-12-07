package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.DateHelper;

public class RepairBookedInDateOnFriday implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(RepairBookedInDateOnFriday.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (claim.getBreBand().isRepairBookedInDate() && claim.getHireMonitoringDetail() != null) {
            boolean success = true;

            if (claim.getHireMonitoringDetail().getRepairBookInDate() != null && claim.getCustomer().getIsUsable()) {

                if (DateHelper.getDayOfWeek(claim.getHireMonitoringDetail().getRepairBookInDate()) == 6) {
                    success = false;
                    narrative = "Repair booked in on Friday and the CHO's Customer's vehicle was driveable.";
                }
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
        return "026";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
