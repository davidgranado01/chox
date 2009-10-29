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
    ClaimStatus statusAfterFailure = ClaimStatus.InvoiceEscalated;
    String narrativeTemplate = "The Hire Net billed %s exceeds the Hire Net ceiling of %s for vehicle class %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        /*
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
        */

        if(claim.getChoBand().isHireNetDoesNotExceedVehicleClassHireNetCeiling()){

            BigDecimal hireNet = claim.getInvoice().getHireNet();

            // CHECK BRE BAND > HIRE NET
            BigDecimal hireNetCeiling = claim.getChoBand().getHireNetCeiling();
            boolean success = hireNet.compareTo(hireNetCeiling) <= 0;

            if(success){

                res.setResult(RuleEvaluationResult.RulePassed);

                // CHECK VEHICLE CLASS CEILLING > HIRE NET
                if(claim.getChoBand().isVehicleClassCellingEnable()){

                    hireNetCeiling = claim.getChoBand().getMaxHireNetCeiling();
                    success = hireNet.compareTo(hireNetCeiling) <= 0;
                    res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

                    if(!success){
                        statusAfterFailure = ClaimStatus.InvoiceEscalatedToHandler;
                    }

                }

            }else{

                res.setResult(RuleEvaluationResult.RuleFailed);
                narrativeTemplate = "The Hire Net billed %s exceeds the Hire Net ceiling of %s";

            }

            if(!success){
                narrative = String.format(narrativeTemplate, moneyFormat.format(hireNet.doubleValue()), moneyFormat.format(hireNetCeiling.doubleValue()), claim.getVClass().getCode());
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
        // return ClaimStatus.InvoiceEscalatedToHandler;
        return statusAfterFailure;
    }
}