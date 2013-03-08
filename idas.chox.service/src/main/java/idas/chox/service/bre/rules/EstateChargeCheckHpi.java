package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class EstateChargeCheckHpi implements IBusinessRule {

     private static final Logger LOG = LoggerFactory.getLogger(EstateChargeCheckHpi.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isEstateChargeCheckHpi() && claim.getInvoice().getEstateFee() != null
                && claim.getInvoice().getEstateFee().compareTo(BigDecimal.ZERO) != 0) {

            LOG.debug("EstateChargeCheckHpi is activated");
            boolean success = true;
            if (claim.getVehicleHire() == null || claim.getVehicleHire().getHpiVehicleDoorplan() == null || !claim.getVehicleHire().getHpiVehicleDoorplan().equals("Estate")) {
                success = false;
                narrative = "The CHO is charging an estate fee for hire and the HPI lookup did not identify the hire vehicle to be an estate, please review need.";
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
        return "061";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
