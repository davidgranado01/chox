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
import idas.chox.core.services.ClaimService;

public class SubscriberAcquisitionFeeCheck implements IBusinessRule {
    
    private static final Logger LOG = LoggerFactory.getLogger(SubscriberAcquisitionFeeCheck.class);
    private ClaimService claimService;
    private String narrative = "";
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    
    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        boolean success = true;

        if (ClaimType.isSubscriber(claim.getClaimType())
                && claim.getBreBand().isSubscriberAcquisitionFeeCheck()) {

            LOG.debug("SubscriberAcquisitionFeeCheck is activated");

            if (claimService.isSubscriberClaimRejectedAndAgreed(claim.getId())) {
                if (claim.getInvoice().getMiscellaneousFee() != null && claim.getInvoice().getMiscellaneousFee().compareTo(BigDecimal.ZERO) !=0) {
                    success = false;
                    narrative = "The CHO is charging an Acquisition Fee however the Subscriber rejection was accepted and therefore this charge should not be made.";
                }

                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
            } else {
                LOG.debug("Subscriber claim was not rejected and agreed - rule skipped");
                narrative = "";
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);                
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
        return "072";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
