
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
        
        if(!claim.getCHOrg().getIsDelegatedAuthority()){
            IInvoiceInfo invoice = claim.getInvoice();
            IInsurerInfo insurer = claim.getInsurer();
            
            BigDecimal adminHandlingCharge = new BigDecimal("0.00");
            if(insurer.getAdminHandlingCharge().doubleValue()>0 && CalcHelper.VAT_RATE.doubleValue()>0){
                adminHandlingCharge = (insurer.getAdminHandlingCharge()).multiply(CalcHelper.VAT_RATE).negate();
            }
            
            System.out.println("new adminHandlingCharge:" + adminHandlingCharge);
            
            boolean success = CalcHelper.EqualTo(invoice.getDiscount(), adminHandlingCharge);
            
            if(success) narrative = "";
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);    
        }
        else{
            res.setResult(RuleEvaluationResult.RuleSkipped);
            narrative = "Rule does not apply to CHOs in the DA scheme";
        }
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        
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

