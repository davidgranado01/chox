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
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.Invoice;

public class RepairGrossIsLessThanEstimatedTotalRepairAmount implements IBusinessRule {
    private static final Logger LOG = LoggerFactory.getLogger(RepairGrossIsLessThanEstimatedTotalRepairAmount.class);

    private String narrative = "Repair Gross is higher than the Estimated Total Repair Amount.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {
        LOG.debug("Applying rule 'RepairGrossIsLessThanEstimatedTotalRepairAmount' to claim {}.", claim.getChoReference());

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isCollaborationProtocol(claim.getClaimType()) && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isRepairGrossIsLessThanEstimatedTotalRepairAmount() && claim.getEngineerReport() != null) {

            EngineerReport eReport = claim.getEngineerReport();
            Invoice invoice = claim.getInvoice();

            boolean success;

            if (eReport.getEstimatedTotalRepairAmount() != null && eReport.getEstimatedTotalRepairAmount().compareTo(BigDecimal.ZERO) > 0) {

                success = invoice.getRepairGross().compareTo(eReport.getEstimatedTotalRepairAmount()) <= 0;
                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            } else {
                success = true;
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                // success = CalcHelper.EqualTo(invoice.getRepairGross(), BigDecimal.ZERO);
            }

            if (success) {
                narrative = "";
            }else{
                narrative = "Repair Gross is higher than the Estimated Total Repair Amount.";
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
        return "008";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
