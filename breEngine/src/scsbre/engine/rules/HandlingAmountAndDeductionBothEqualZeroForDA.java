/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import java.math.BigDecimal;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.engine.util.CalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;
import scsbre.model.IInvoiceInfo;

/**
 *
 * @author Derm
 * 
 * rule 16, order 10
 */
public class HandlingAmountAndDeductionBothEqualZeroForDA implements IBusinessRule {

    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        
        if(claim.getCHOrg().getIsDelegatedAuthority()){
            IInvoiceInfo invoice = claim.getInvoice();
            
            boolean success = CalcHelper.EqualTo(invoice.getClaimsHandlingInvoiceAmount(), BigDecimal.ZERO);
            success = success && CalcHelper.EqualTo(invoice.getDeductionForClaimsHandlingFee(), BigDecimal.ZERO);  
           
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);    
        }
        else{
            res.setResult(RuleEvaluationResult.RuleSkipped);
        }
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        
        return res;

    }  

    public String getFailureMessage() {
        return "CHO is on DA scheme.  Discount should be 0.";
    }

    public String getRuleId() {
        return "016";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.AwaitingPaymentPack;
    }

}
