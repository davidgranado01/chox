package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class BlankLabourRateCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isBlankLabourRateCheck()) {
            boolean success = true;

            if (claim.getInvoice().getRepairNet().compareTo(BigDecimal.ZERO) != 0 && (claim.getHireMonitoringDetail().getLabourRate() == null || claim.getHireMonitoringDetail().getLabourRate().compareTo(BigDecimal.ZERO) == 0)) {
                success = false;
                narrative = "No repair allowed when there is no Labour Rate provided.";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

        } else {
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
        return "099";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
