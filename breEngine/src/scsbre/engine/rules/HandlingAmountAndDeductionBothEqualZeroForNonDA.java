package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.IInvoiceInfo;

public class HandlingAmountAndDeductionBothEqualZeroForNonDA implements IBusinessRule {

    String narrative = "Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0.";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if(claim.getChoBand().isHandlingAmountAndDeductionBothEqualZeroForNonDA()){

            if(!claim.getCHOrg().isDelegatedAuthority()){

                IInvoiceInfo invoice = claim.getInvoice();
                boolean success = CalcHelper.EqualTo(invoice.getClaimsHandlingInvoiceAmount(), BigDecimal.ZERO);
                success = success && CalcHelper.EqualTo(invoice.getDeductionForClaimsHandlingFee(), BigDecimal.ZERO);
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if(success) {narrative = "";}

            }else{

                res.setResult(RuleEvaluationResult.RuleSkipped);
                narrative = "Rule does not apply to CHOs in the DA scheme";
                
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
        return "016";
    }

    public ClaimStatus getStatusAfterFailure() {
        // CARLSON @ 20091012
        // HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim)) STATUS = InvoiceDataCalculationIncorrect;
        // return ClaimStatus.InvoiceEscalated;
        return ClaimStatus.InvoiceDataCalculationIncorrect;
    }

}
