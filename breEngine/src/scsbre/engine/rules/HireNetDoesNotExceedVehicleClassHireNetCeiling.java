/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scsbre.engine.rules;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import scsbre.engine.IBusinessRule;
import scsbre.engine.RuleEvaluation;
import scsbre.engine.RuleEvaluationResult;
import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

/**
 *
 * @author Derm
 * 
 * rule 3, order 13
 * 
 */
public class HireNetDoesNotExceedVehicleClassHireNetCeiling implements IBusinessRule {

    String narrative = "";
    String narrativeTemplate = "The Hire Net billed %s exceeds the Hire Net ceiling of %s for vehicle class %s.";

    public RuleEvaluation applyToClaim(IClaimInfo claim) {

        BigDecimal hireNet = claim.getInvoice().getHireNet();
        BigDecimal hireNetCelling = claim.getVehicleClassCellingInfo().getHireNetCelling();
        boolean success = hireNet.compareTo(hireNetCelling) <= 0;

        RuleEvaluation res = new RuleEvaluation();
        res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        DecimalFormat moneyFormat = new DecimalFormat("£0.00");
        narrative = String.format(narrativeTemplate, moneyFormat.format(hireNet.doubleValue()), moneyFormat.format(hireNetCelling.doubleValue()), claim.getVClass().getCode());

        return res;

    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "003";
    }

    public ClaimStatus getStatusAfterFailure() {
        return ClaimStatus.InvoiceEscalated;
    }
}
