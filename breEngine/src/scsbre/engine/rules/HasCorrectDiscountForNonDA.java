
package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.IInsurerInfo;
import scsbre.model.IInvoiceInfo;

public class HasCorrectDiscountForNonDA implements IBusinessRule {

    String narrative = "Discount calculation is incorrect";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if(claim.getBreBand().isHasCorrectDiscountForNonDA()){
            
            if(!claim.getCHOrg().isDelegatedAuthority()){

                IInvoiceInfo invoice = claim.getInvoice();
                IInsurerInfo insurer = claim.getInsurer();

                BigDecimal adminHandlingCharge = new BigDecimal("0.00");
                if(insurer.getAdminHandlingCharge().doubleValue()>0 && CalcHelper.VAT_RATE.doubleValue()>0){
                    adminHandlingCharge = (insurer.getAdminHandlingCharge()).multiply(CalcHelper.VAT_RATE).negate();
                }

                boolean success = CalcHelper.EqualTo(invoice.getDiscount(), adminHandlingCharge);
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

                if(success){ narrative = "";}

            }else{

                narrative = "Rule does not apply to CHOs in the DA scheme";
                res.setResult(RuleEvaluationResult.RuleSkipped);
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
        return "015";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceDataCalculationIncorrect;
    }    

}

