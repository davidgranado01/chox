
package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.IInsurerInfo;
import scsbre.model.IInvoiceInfo;

/**
 * rule 15, order 9
 * @author Derm
 */
public class HasCorrectDiscountForNonDA implements IBusinessRule {


    String narrative = "Discount calculation is incorrect";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        RuleEvaluation res = new RuleEvaluation();
        if(!claim.getCHOrg().getIsDelegatedAuthority()){
            IInvoiceInfo invoice = claim.getInvoice();
            IInsurerInfo insurer = claim.getInsurer();
            boolean success = CalcHelper.EqualTo(invoice.getDiscount(),
                (insurer.getAdminHandlingCharge()).multiply(CalcHelper.VAT_RATE).negate());
            
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

