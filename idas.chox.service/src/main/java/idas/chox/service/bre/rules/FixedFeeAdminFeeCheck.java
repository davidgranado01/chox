package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class FixedFeeAdminFeeCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(FixedFeeAdminFeeCheck.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());


       
        if (ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isFixedFeeAdminFeeCheck()) {

            LOG.debug("FixedFeeAdminFeeCheck is activated");
            boolean managingRepair = claim.getManagingRepair();

            if (!managingRepair && claim.getInvoice().getAdminFee() != null && claim.getInvoice().getAdminFee().compareTo(claim.getBreBand().getAdminFeeCeilingFixedFee()) > 0) {
                    narrative = "The Admin Fee billed is incorrect. The allowed Admin Fee for Fixed-Fee claims not managing the repair is £" + claim.getBreBand().getAdminFeeCeilingFixedFee() + ".";
                    res.setResult(RuleEvaluationResult.RULE_FAILED);
            } else if (managingRepair && claim.getInvoice().getAdminFee() != null && claim.getInvoice().getAdminFee().compareTo(claim.getBreBand().getAdminFeeCeilingFixedFeeManagingRepair()) > 0) {
                    narrative = "The Admin Fee billed is incorrect. The allowed Admin Fee for Fixed-Fee claims managing the repair is £" + claim.getBreBand().getAdminFeeCeilingFixedFeeManagingRepair() + ".";
                    res.setResult(RuleEvaluationResult.RULE_FAILED);
            } else {
                narrative = "";
                res.setResult(RuleEvaluationResult.RULE_PASSED);                
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
        return "078";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
