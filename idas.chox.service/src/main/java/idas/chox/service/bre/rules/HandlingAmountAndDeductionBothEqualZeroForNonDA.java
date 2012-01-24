package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Invoice;
import idas.chox.service.bre.util.CalcHelper;
import java.math.BigDecimal;

public class HandlingAmountAndDeductionBothEqualZeroForNonDA implements IBusinessRule {

    String narrative = "Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if (claim.getBreBand().isHandlingAmountAndDeductionBothEqualZeroForNonDA()) {

            if (!ClaimType.isSubscriber(claim.getClaimType()) && !claim.getChorganisation().isDelegatedAuthority()) {

                Invoice invoice = claim.getInvoice();
                boolean success = CalcHelper.EqualTo(invoice.getClaimsHandlingInvoiceAmount(), BigDecimal.ZERO);
                success = success && CalcHelper.EqualTo(invoice.getDeductionForClaimsHandlingFee(), BigDecimal.ZERO);
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    narrative = "";
                }else{
                    narrative = "Entries against Claims Handling Invoice Amount and Less Claims Handling Fee are not 0.";
                }

            } else {

                res.setResult(RuleEvaluationResult.RuleSkipped);
                narrative = "Rule does not apply to CHOs in the DA scheme";

            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

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
    public String getStatusAfterFailure(boolean isTpiClaim) {
        // CARLSON @ 20091012
        // HandlingAmountAndDeductionBothEqualZeroForNonDA().applyToClaim(claim)) STATUS = InvoiceDataCalculationIncorrect;
        // return ClaimStatus.INVOICE_ESCALATED;
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
