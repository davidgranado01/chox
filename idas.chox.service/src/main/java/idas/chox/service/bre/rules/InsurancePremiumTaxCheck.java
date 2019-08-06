package idas.chox.service.bre.rules;

import java.math.BigDecimal;
import java.math.RoundingMode;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

/*
 * Rajareddy Dodda
 */
public class InsurancePremiumTaxCheck implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(true);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (!ClaimType.isFixedFee(claim.getClaimType())
                && claim.getBreBand().isInsurancePremiumTaxCheck() && claim.getInvoice().getNonStandardInsurancePremiumFee().compareTo(BigDecimal.ZERO) > 0 &&
                 claim.getInvoice().getNonStandardInsurancePremiumQty() > 0) {

            boolean success = true;

            BigDecimal nonStandPremiumFee = claim.getInvoice().getNonStandardInsurancePremiumFee();
            int nonStandPremiumFeeQty = claim.getInvoice().getNonStandardInsurancePremiumQty();

            BigDecimal nonStandardPremium = claim.getBreBand().getNonStandardInsurancePremium();

            BigDecimal nonStandPremiumFeePerDay = nonStandPremiumFee.divide(new BigDecimal(nonStandPremiumFeeQty), 2, RoundingMode.HALF_UP);

            if (nonStandardPremium != null && nonStandPremiumFeePerDay.compareTo(nonStandardPremium) > 0) {
                success = false;
                narrative = "The CHO is charging £" + nonStandPremiumFeePerDay + " per day for the Insurance Premium Tax/Non Standard Risk Insurance Premium Tax, please review.";
            }

            res.setResult(success ? RuleEvaluationResult.RULE_PASSED : RuleEvaluationResult.RULE_FAILED);

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
        return "069";


    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;
    }
}
