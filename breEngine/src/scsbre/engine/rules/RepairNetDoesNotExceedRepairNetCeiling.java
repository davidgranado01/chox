package scsbre.engine.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class RepairNetDoesNotExceedRepairNetCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Repair Net billed %s exceeds the Repair Net ceiling of %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getBreBand().isRepairNetDoesNotExceedBandRepairNetCeiling()){

            BigDecimal repairNet = claim.getInvoice().getRepairNet();
            BigDecimal repairNetCeiling = claim.getBreBand().getRepairNetCeiling();
            boolean success = repairNet.compareTo(repairNetCeiling) <= 0;
            
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if(!success){
                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(repairNet.doubleValue()),
                        moneyFormat.format(repairNetCeiling.doubleValue()));
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
        return "041";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }
}