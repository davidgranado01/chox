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

/**
 *
 * @author Derm rule 17, order 10
 */
public class ClaimHasZeroDiscountForDA implements IBusinessRule{

    
    String narrative = "CHO is on DA scheme. Discount should be 0.";
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        RuleEvaluation res = new RuleEvaluation();
        
        res.setIsVisibleToCHO(true);
        
        if(claim.getCHOrg().getIsDelegatedAuthority()){
            boolean success = CalcHelper.EqualTo(claim.getInvoice().getDiscount(), BigDecimal.ZERO);
            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
            if(success) narrative = "";
            res.setIsVisibleToCHO(false);
        }
        else{
            narrative = "Rule does not apply to CHOs not in the DA scheme";
            res.setResult(RuleEvaluationResult.RuleSkipped);
        }
        
        res.setRelatedRule(this);
        
        return res;

    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
       return "017";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }

}
