package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public class CorrentAdminFee implements IBusinessRule {

    private String narrative = "";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        
        if(claim.getBreBand().isCorrentAdminFee()){

            boolean success = true;
            BigDecimal adminFee = new BigDecimal("40.00");

            if(claim.getManagingRepair()){
                adminFee = new BigDecimal("60.00");
            }

            if(claim.getInvoice().getAdminFee().compareTo(adminFee)>=1){
                success = false;
                narrative = "The Admin Fee billed is incorrect.";
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
            
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
        return "025";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceDataCalculationIncorrect;
    }

}