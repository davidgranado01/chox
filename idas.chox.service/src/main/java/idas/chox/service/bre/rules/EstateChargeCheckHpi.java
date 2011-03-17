package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EstateChargeCheckHpi implements IBusinessRule {

     private static final Logger LOG = LoggerFactory.getLogger(EstateChargeCheckHpi.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isEstateChargeCheckHpi()) {

            LOG.debug("EstateChargeCheckHpi is activated");
            boolean success = true;
            if (!claim.getVehicleHire().getHpiVehicleDoorplan().equals("Estate")) {
                success = false;
                narrative = "The CHO is charging an estate fee for the hire and the HPI lookup did not identify the Customer's vehicle to be an estate, please review need.";
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
        return "061";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
