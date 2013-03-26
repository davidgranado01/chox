package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.util.CalcHelper;
import idas.chox.service.bre.util.InvoiceCalcHelper;

/**
 *
 * @author Derm
 * 
 * rule 5, order 5
 */
public class HasCorrectHireGrossCalculation implements IBusinessRule {

    private String narrative = "Hire Gross calculation is incorrect.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isHasCorrectHireGrossCalculation()) {

            InvoiceCalcHelper iCalc = InvoiceCalcHelper.getInstance(claim.getInvoice());
            boolean success = CalcHelper.equalTo(claim.getInvoice().getHireGross(), iCalc.getCalculatedHireGross());

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

            if (success) {
                narrative = "";
            }else{
                narrative = "Hire Gross calculation is incorrect.";
            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RULE_SKIPPED);

        }

        return res;

    }

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "005";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
