package scsbre.engine.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class RepairNetDoesNotExceedVehicleClassRepairNetCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Repair Net billed %s exceeds the Repair Net ceiling of %s for vehicle class %s.";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        BigDecimal repairNet = claim.getInvoice().getRepairNet();
        BigDecimal repairNetCelling = claim.getChoBand().getMaxRepairValueCelling();
        boolean success = repairNet.compareTo(repairNetCelling) <= 0;

        RuleEvaluation res = new RuleEvaluation();
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        DecimalFormat moneyFormat = new DecimalFormat("£0.00");
        narrative = String.format(narrativeTemplate, moneyFormat.format(repairNet.doubleValue()), moneyFormat.format(repairNetCelling.doubleValue()), claim.getVClass().getCode());

        return res;

    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "023";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
        // TODO: CHECK STATUS
        // return ClaimStatus.InvoiceEscalatedToHandler;
    }
}
