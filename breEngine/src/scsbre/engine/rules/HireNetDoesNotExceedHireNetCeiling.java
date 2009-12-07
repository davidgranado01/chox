package scsbre.engine.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class HireNetDoesNotExceedHireNetCeiling implements IBusinessRule {

    String narrative = "";
    ClaimStatus statusAfterFailure = ClaimStatus.InvoiceEscalatedToHandler;
    String narrativeTemplate = "The Hire Net billed %s exceeds the Hire Net ceiling of %s";
    DecimalFormat moneyFormat = new DecimalFormat("£0.00");

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        
        if(claim.getBreBand().isHireNetDoesNotExceedBandHireNetCeiling()){
        
            boolean success = claim.getInvoice().getHireNet().compareTo(claim.getBreBand().getHireNetCeiling()) <= 0;
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if(!success){
                
                narrative = String.format(narrativeTemplate,
                        moneyFormat.format(claim.getInvoice().getHireNet().doubleValue()),
                        moneyFormat.format(claim.getBreBand().getHireNetCeiling().doubleValue()));
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
        return "040";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalatedToHandler;
    }    
}