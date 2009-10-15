/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.IClaimInfo;
import scsbre.engine.util.CalcHelper;
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.ClaimStatus;
/**
 *
 * @author Derm
 * 
 * rule 5, order 5
 */
public class HasCorrectHireGrossCalculation implements IBusinessRule {

    private String narrative = "Hire Gross calculation is incorrect.";
    public RuleEvaluation applyToClaim(IClaimInfo claim) {
        
        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if(claim.getChoBand().isHasCorrectHireGrossCalculation()){

            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
            boolean success = CalcHelper.EqualTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if(success){
                narrative = "";
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
       return "005";
    }
    
    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceDataCalculationIncorrect;
    }

}
