
package scsbre.engine.rules;

import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.IClaimInfo;
import scsbre.engine.util.CalcHelper;
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.ClaimStatus;
import scsbre.model.IInvoiceInfo;

/**
 *
 * @author Derm
 * 
 * rule 14, order 7
 */
public class HasCalculatedTotalGrossEqualSuppliedTotalGross implements IBusinessRule {

    private String narrative = "Total Gross calculation is incorrect.";
    
    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        if(claim.getBreBand().isHasCalculatedTotalGrossEqualSuppliedTotalGross()){
            
            IInvoiceInfo invoice = claim.getInvoice();
            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
            boolean success = CalcHelper.EqualTo(invoice.getTotalGross(), iCalc.getCalculatedTotalGross());

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

            if(success){narrative="";}

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
        return "014";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceDataCalculationIncorrect;
    }
}

