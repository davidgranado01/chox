package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.CalcHelper;

public class NumberOfHireDaysReconcile implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(NumberOfHireDaysReconcile.class);
    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isNumberOfHireDaysReconcile() && claim.getVehicleHire() != null) {

            boolean success = true;

            int dayDif = (CalcHelper.getDaysBetweenDates(claim.getVehicleHire().getRentalStart(), claim.getVehicleHire().getRentalEnd()) + 1);

            if (claim.getVehicleHire().getDays() > 0 && claim.getVehicleHire().getDays() != dayDif) {
                LOG.debug("Rule failed: {} != {}", claim.getVehicleHire().getDays(), dayDif);
                success = false;
                narrative = "The number of Hire Days billed does not reconcile with the Hire Start and Hire End dates provided";
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
        return "024";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
