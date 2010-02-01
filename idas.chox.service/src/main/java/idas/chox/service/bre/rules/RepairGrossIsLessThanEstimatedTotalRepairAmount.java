package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.model.Invoice;
import java.math.BigDecimal;

public class RepairGrossIsLessThanEstimatedTotalRepairAmount implements IBusinessRule {

    private String narrative = "Repair Gross is higher than the Estimated Total Repair Amount.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isRepairGrossIsLessThanEstimatedTotalRepairAmount()) {

            EngineerReport eReport = claim.getEngineerReport();
            Invoice invoice = claim.getInvoice();

            boolean success;

            if (eReport.getEstimatedTotalRepairAmount().compareTo(BigDecimal.ZERO) > 0) {

                success = invoice.getRepairGross().compareTo(eReport.getEstimatedTotalRepairAmount()) <= 0;
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            } else {
                success = true;
                res.setResult(RuleEvaluationResult.RuleSkipped);
                // success = CalcHelper.EqualTo(invoice.getRepairGross(), BigDecimal.ZERO);
            }

            if (success) {
                narrative = "";
            }else{
                narrative = "Repair Gross is higher than the Estimated Total Repair Amount.";
            }

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
        return "008";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED;
    }
}
