package scsbre.engine.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class HireNetDoesNotExceedVehicleClassHireNetCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Hire Net billed %s exceeds the Hire Net ceiling of %s for vehicle class %s.";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getChoBand().isHireNetDoesNotExceedVehicleClassHireNetCeiling()){

            BigDecimal hireNet = claim.getInvoice().getHireNet();
            BigDecimal hireNetCelling = claim.getChoBand().getMaxHireNetCeiling();
            boolean success = hireNet.compareTo(hireNetCelling) <= 0;

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if(!success){
                DecimalFormat moneyFormat = new DecimalFormat("£0.00");
                narrative = String.format(narrativeTemplate, moneyFormat.format(hireNet.doubleValue()), moneyFormat.format(hireNetCelling.doubleValue()), claim.getVClass().getCode());
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
        return "003";
    }

    public ClaimStatus getStatusAfterFailure() {
        // return ClaimStatus.InvoiceEscalated;
        return ClaimStatus.InvoiceEscalatedToHandler;
    }
}