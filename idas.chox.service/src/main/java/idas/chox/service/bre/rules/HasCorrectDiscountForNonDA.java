
package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.service.bre.util.CalcHelper;
import java.math.BigDecimal;

public class HasCorrectDiscountForNonDA implements IBusinessRule {

    String narrative = "Discount calculation is incorrect";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setIsTPIClaim(ClaimType.isTPI(claim.getClaimType()));

        if(!ClaimType.isSubscriber(claim.getClaimType()) && claim.getBreBand().isHasCorrectDiscountForNonDA()){

            if(!claim.getChorganisation().isDelegatedAuthority()){

                Invoice invoice = claim.getInvoice();
                Insurer insurer = claim.getInsurer();

                BigDecimal adminHandlingCharge = new BigDecimal("0.00");
                if(insurer.getAdminHandlingCharge().doubleValue()>0 && CalcHelper.VAT_RATE.doubleValue()>0){
                    adminHandlingCharge = (insurer.getAdminHandlingCharge()).multiply(CalcHelper.VAT_RATE).negate();
                }

                boolean success = CalcHelper.EqualTo(invoice.getDiscount(), adminHandlingCharge);
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

                if(success){
                    narrative = "";
                }else{
                    narrative = "Discount calculation is incorrect";
                }

            }else{

                narrative = "Rule does not apply to CHOs in the DA scheme";
                res.setResult(RuleEvaluationResult.RuleSkipped);
            }

        }else{

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
        return "015";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }

}

