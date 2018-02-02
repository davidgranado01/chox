package idas.chox.service.bre.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class CopleyOfferMadeCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(CopleyOfferMadeCheck.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        boolean success = true;

        if (ClaimType.isInsurerUpload(claim.getClaimType()) && claim.getInsurer().isCopleyQuestion() && claim.getBreBand().isCopleyOfferMadeCheck()) {

            if (claim.isCopleyOfferMade() != null && claim.isCopleyOfferMade()) {
                success = false;
                narrative = "The CHO is claiming when a Copley Offer has been made on this claim.";
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
        return "096";
    }

    
    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
