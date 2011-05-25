/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;

public class ValidateUniqueVehicleRegistrationNumber implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setIsTPIClaim(claim.isTpiClaim());

        if (claim.getBreBand().isValidateUniqueVehicleRegistrationNumber()) {

            Customer Icust = claim.getCustomer();

            Boolean isVehicleRegistrationExist = Icust.isVehicleRegistrationExist();

            res.setResult(isVehicleRegistrationExist ? RuleEvaluationResult.RuleFailed : RuleEvaluationResult.RulePassed);

            if (isVehicleRegistrationExist) {
                narrative = "The Customer's Vehicle Registration Number supplied already exists in the system.";
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
        return "021";
    }

    @Override
    public String getStatusAfterFailure(boolean isTpiClaim) {
        return "InvoiceVehicleRegistrationNotUnique";
    }
}
