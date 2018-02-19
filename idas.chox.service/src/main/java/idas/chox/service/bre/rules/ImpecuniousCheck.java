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

public class ImpecuniousCheck implements IBusinessRule {

    private static final Logger LOG = LoggerFactory.getLogger(ImpecuniousCheck.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        boolean success = true;

        if (claim.getBreBand().isImpecuniousCheck() && claim.getInsurerHireMonitoringDetail() != null && claim.getInsurerHireMonitoringDetail().getClaimantImpecunious() != null
                && ((ClaimType.isInsurerUpload(claim.getClaimType()) && claim.getInsurer().isEnableManualLouDates())
                    || (!ClaimType.isInsurerUpload(claim.getClaimType()) && claim.getInsurer().isEnableLouDates()))) {
            if (!claim.getInsurerHireMonitoringDetail().getClaimantImpecunious() && (claim.getBreBand().getImpecuniousStartDate() == null || (claim.getBreBand().getImpecuniousStartDate() != null
                    && (claim.getCreatedDate().after(claim.getBreBand().getImpecuniousStartDate()) || claim.getCreatedDate().equals(claim.getBreBand().getImpecuniousStartDate()))))
                && (claim.getInvoice().getHireGross().compareTo(BigDecimal.ZERO) > 0 || claim.getInvoice().getRepairGross().compareTo(BigDecimal.ZERO) > 0)) {
                success = false;
                narrative = "The CHO is claiming for Hire and/or Repair and the claimant is flagged as being pecunious.";
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
        return "097";
    }

    
    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
