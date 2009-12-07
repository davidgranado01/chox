package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.IInvoiceInfo;

public class HasCorrectTotalVat implements IBusinessRule{

    private String narrative = "Total VAT calculation is incorrect.";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if(claim.getBreBand().isHasCorrectTotalVat()){

            IInvoiceInfo invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(invoice);
            boolean success = CalcHelper.EqualTo(invoice.getTotalVat(), iCalc.getCalculatedTotalVat());
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if(success){narrative = "";}

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
        return "013";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceDataCalculationIncorrect;
    }
    

}
