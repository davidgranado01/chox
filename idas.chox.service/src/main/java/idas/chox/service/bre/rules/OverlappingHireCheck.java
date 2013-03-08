package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.ClaimService;

public class OverlappingHireCheck implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(OverlappingHireCheck.class);

    private String narrative = "";
    private ClaimService claimService;
    
    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isOverlappingHireCheck() && claim.getVehicleHire() != null) {
            LOG.debug("In rule OverlappingHireCheck for claim {}....", claim.getChoReference());
            /*
             * This rule will look at the hire/replacement vehicle's VRN, the VRN
             * will be compared to all claims submitted against the said Insurer only,
             * if there is a match then the rule will then look at the 'Hire Start'
             * and 'Hire End' dates if the same VRN appears on more than one claim
             * across any of the same dates then the rule should fail.
             * Supplementary invoices for claims for the same CHO and any claims
             * where the hire/replacement vehicle VRN is 'NK1' should be ignored.
             * 
             */
            LOG.debug("In rule OverlappingHireCheck for claim {}....", claim.getChoReference());
            String insurerClaimNumber = claimService.getOverlappingHire(claim);
            boolean success = true;
            
            if (insurerClaimNumber != null) {
                success = false;
                narrative = "Overlapping Hire - replacement hire vehicle on hire during overlapping periods as indicated on claim " + insurerClaimNumber + ".";
            } else {
                LOG.debug("OverlappingHireCheck BRE rule passed for claim {}....", claim.getChoReference());
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
        } else {
            LOG.debug("OverlappingHireCheck BRE rule skipped for claim {}....", claim.getChoReference());
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
        return "076";
    }


    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

}
