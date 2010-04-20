package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.service.bre.util.ClaimCalcHelper;
import java.math.BigDecimal;

public class LabourCostBusinessRule implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isLabourCostBusinessRule()) {

            boolean success = true;
            ClaimCalcHelper cCalc = ClaimCalcHelper.getInstance(claim);

            if (isRequiredToValidateByBRE(claim)) {

                int iNumberOfHireDay = claim.getVehicleHire().getDays();
                int iNumberDayOfLabourCostWorthy = cCalc.getNumberDayOfLabourCostWorthy();

                if (iNumberOfHireDay > iNumberDayOfLabourCostWorthy) {
                    success = false;
                    narrative = "The number of hire days billed by the CHO is not relative to the number of expected hire days based on the labour information provided.";
                }

                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            } else {

                narrative = "Insufficient information to perform labour cost rule.";
                res.setResult(RuleEvaluationResult.RuleSkipped);

            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }

        return res;
    }

    private boolean isRequiredToValidateByBRE(Claim claim) {

        boolean bFlag = true;

        if (claim.getHireMonitoringDetail() != null) {

            BigDecimal bLabourCost = BigDecimal.ZERO;
            BigDecimal iLabourHour = BigDecimal.ZERO;

            if (claim.getHireMonitoringDetail().getLabourCost() != null) {
                bLabourCost = claim.getHireMonitoringDetail().getLabourCost();
            }

            if (claim.getHireMonitoringDetail().getLabourHour() != null) {
                iLabourHour = claim.getHireMonitoringDetail().getLabourHour();
            }

            if ((bLabourCost.compareTo(BigDecimal.ZERO) < 1) && (iLabourHour.compareTo(BigDecimal.ZERO) < 1)) {
                bFlag = false;
            }

        } else {
            bFlag = false;
        }

        return bFlag;
    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "022";
    }

    @Override
    public String getStatusAfterFailure() {

        // LabourCostBusinessRule().applyToClaim(claim)) STATUS = InvoiceEscalatedToHandler;
        // return ClaimStatus.INVOICE_ESCALATED;

        // CARLSON @ 20091012
        // return ClaimStatus.INVOICE_ESCALATED_TO_CH; 

        // CARLSON @ 20091028
        return ClaimStatus.INVOICE_ESCALATED;
    }
}
