
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

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        RuleEvaluation res = new RuleEvaluation();

        IInvoiceInfo invoice = claim.getInvoice();
        InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
        boolean success = CalcHelper.EqualTo(invoice.getTotalGross(), iCalc.getCalculatedTotalGross());

        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);

        return res;

    }

    public String getFailureMessage() {
        return "Total Gross calculation is incorrect.";
    }

    public String getRuleId() {
        return "014";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.AwaitingPaymentPack;
    }
}

