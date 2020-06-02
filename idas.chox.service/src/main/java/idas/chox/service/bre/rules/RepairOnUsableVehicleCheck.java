package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


public class RepairOnUsableVehicleCheck implements IBusinessRule {
    
    private static final Logger LOG = LoggerFactory.getLogger(RepairOnUsableVehicleCheck.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        //Default
            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);


        if ((ClaimType.isGTA_WideDef(claim.getClaimType()) || ClaimType.isInsurerUpload(claim.getClaimType()))
                && claim.getBreBand().isRepairChargeCheck()) {
            if (claim.getCustomer() != null && claim.getCustomer().getIsUsable() != null && claim.getCustomer().getIsUsable()
                    && claim.getInvoice() != null && claim.getInvoice().getRepairNet() != null) {

                BigDecimal repairNet = claim.getInvoice().getRepairNet();
                boolean success = repairNet.compareTo(BigDecimal.ZERO) <= 0;

                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

                if (!success) {
                    narrative = "The CHO is claiming for repair and the vehicle is marked is usable";
                }
            } else {
                res.setResult(RuleEvaluationResult.RULE_PASSED);
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
        return "110";


    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {

        return ClaimStatus.INVOICE_ESCALATED_TO_CH;

    }
}
