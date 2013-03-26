package idas.chox.service.bre.rules;

import java.math.BigDecimal;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Invoice;
import idas.chox.core.util.CalcHelper;

public class HandlingAmountAndDeductionBothEqualZeroForNonDA implements IBusinessRule {

    String narrative = "Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isHandlingAmountAndDeductionBothEqualZeroForNonDA()) {

            if (!ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())
                && !claim.getChorganisation().isDelegatedAuthority()) {

                Invoice invoice = claim.getInvoice();
                boolean success = CalcHelper.equalTo(invoice.getClaimsHandlingInvoiceAmount(), BigDecimal.ZERO);
                success = success && CalcHelper.equalTo(invoice.getDeductionForClaimsHandlingFee(), BigDecimal.ZERO);
                res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);
                if (success) {
                    narrative = "";
                }else{
                    narrative = "Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0.";
                }
            } else {
                res.setResult(RuleEvaluationResult.RULE_SKIPPED);
                narrative = "Rule does not apply to CHOs in the DA scheme";
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
        return "016";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
