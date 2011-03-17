/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Rajareddy Dodda
 */
public class InsurancePremiumTaxCheck implements IBusinessRule {

    
    private static final Logger LOG = LoggerFactory.getLogger(InsurancePremiumTaxCheck.class);

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isInsurancePremiumTaxCheck()) {

            LOG.debug("Insurance Premium Tax Check  is active");

            boolean success = true;

            BigDecimal nonStandPremiumFee = claim.getInvoice().getNonStandardInsurancePremiumFee();
            int nonStandPremiumFeeQty = claim.getInvoice().getNonStandardInsurancePremiumQty();


            BigDecimal standardPremium = claim.getBreBand().getStandardInsurancePremium();
            BigDecimal nonStandardPremium = claim.getBreBand().getNonStandardInsurancePremium();

            BigDecimal nonStandPremiumFeePerDay = nonStandPremiumFee.divide(new BigDecimal(nonStandPremiumFeeQty));


            LOG.debug(" non Stand Premium Fee  {}. ", nonStandPremiumFee);
            LOG.debug(" non Stand Premium Fee Qty  {} ", nonStandPremiumFeeQty);
            LOG.debug(" standard Premium in Admin BreBand Panel'  {}. ", standardPremium);
            LOG.debug(" nonStandardPremium  in Admin BreBand Panel {} ", nonStandardPremium);
            LOG.debug(" non Stand Premium Fee Per Day {} ", nonStandPremiumFeePerDay);


            if (nonStandPremiumFeePerDay.compareTo(standardPremium) == 0 || nonStandPremiumFeePerDay.compareTo(nonStandardPremium) == 0) {

                narrative = "";

            } else {

                success = false;
                narrative = "The CHO is charging £" + nonStandPremiumFeePerDay + " per day for the Insurance Premium Tax/Non Standard Risk Insurance Premium Tax, please review.";
            }

            res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }
        LOG.debug("Insurance Premium Tax Check  is end");
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
    public String getStatusAfterFailure() {

        return ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT;

    }
}
