package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;

public class HasAllowedVehicleClass implements IBusinessRule {

    String narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";

    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isHasAllowedVehicleClass()) {

            if (claim.getCustomer() != null && claim.getCustomer().getVehicleClass() != null) {

                VehicleClass vehicleClass = claim.getCustomer().getVehicleClass();
                boolean success = vehicleClass.getPrice().compareTo(vehicleClass.getPrice()) <= 0;
                res.setResult(success ? RuleEvaluationResult.RulePassed : RuleEvaluationResult.RuleFailed);
                if (success) {
                    narrative = "";
                }

            } else {

                narrative = "Customer vehicle class is not specified.";
                res.setResult(RuleEvaluationResult.RuleSkipped);

            }

        } else {

            narrative = "";
            res.setResult(RuleEvaluationResult.RuleSkipped);

        }

        return res;
    }

    public String getNarrative() {
        return narrative;
    }

    public String getRuleId() {
        return "001";
    }

    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
