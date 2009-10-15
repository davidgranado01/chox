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

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getChoBand().isRepairNetDoesNotExceedVehicleClassRepairNetCeiling()){

            BigDecimal repairNet = claim.getInvoice().getRepairNet();
            BigDecimal repairNetCelling = claim.getChoBand().getMaxRepairValueCelling();
            boolean success = repairNet.compareTo(repairNetCelling) <= 0;
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
            DecimalFormat moneyFormat = new DecimalFormat("£0.00");
            narrative = String.format(narrativeTemplate, moneyFormat.format(repairNet.doubleValue()), moneyFormat.format(repairNetCelling.doubleValue()), claim.getVClass().getCode());

            if(success){
                narrative = "";
            }

        }else{

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }
        
        return res;

    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "023";
    }

    public ClaimStatus getStatusAfterFailure() {
        // return ClaimStatus.InvoiceEscalated;
        return ClaimStatus.InvoiceEscalatedToHandler;
    }
}