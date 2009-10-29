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
    ClaimStatus statusAfterFailure = ClaimStatus.InvoiceEscalated;
    String narrativeTemplate = "The Repair Net billed %s exceeds the Repair Net ceiling of %s for vehicle class %s.";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if(claim.getChoBand().isRepairNetDoesNotExceedVehicleClassRepairNetCeiling()){

            BigDecimal repairNet = claim.getInvoice().getRepairNet();

            // CHECK BRE BAND > REPAIR NET
            BigDecimal repairNetCeiling = claim.getChoBand().getRepairNetCeiling();
            boolean success = repairNet.compareTo(repairNetCeiling) <= 0;
            
            if(success){

                res.setResult(RuleEvaluationResult.RulePassed);

                // CHECK VEHICLE CLASS CEILLING > REPAIR NET
                if(claim.getChoBand().isVehicleClassCellingEnable()){
                    
                    repairNetCeiling = claim.getChoBand().getMaxRepairNetCelling();                    
                    success = repairNet.compareTo(repairNetCeiling) <= 0;
                    res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

                    if(!success){
                        statusAfterFailure = ClaimStatus.InvoiceEscalatedToHandler;
                    }
                    
                }

            }else{
                narrativeTemplate = "The Repair Net billed %s exceeds the Repair Net ceiling of %s";
                res.setResult(RuleEvaluationResult.RuleFailed);
                
            }

            if(!success){
                narrative = String.format(narrativeTemplate, moneyFormat.format(repairNet.doubleValue()), moneyFormat.format(repairNetCeiling.doubleValue()), claim.getVClass().getCode());
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
        // return ClaimStatus.InvoiceEscalatedToHandler;
        return statusAfterFailure;
    }
}