package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.VehicleClass;
import idas.chox.service.bre.util.VehicleClassHelper;

public class HasAllowedVehicleClass implements IBusinessRule {

    String narrative = "Vehicle class allocated for hire is not a like for like match on the customer's vehicle class.";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);

        if (claim.getBreBand().isHasAllowedVehicleClass()) {

            if (claim.getCustomer() != null && VehicleClassHelper.isVehicleClassValid(claim.getCustomer().getVehicleClass())) {

                VehicleClass vehicleClass = claim.getCustomer().getVehicleClass();
                boolean success = claim.getVehicleHire().getVehicleClass().getPrice().compareTo(vehicleClass.getPrice()) <= 0;
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

    @Override
    public String getNarrative() {
        return narrative;
    }

    @Override
    public String getRuleId() {
        return "001";
    }

    @Override
    public String getStatusAfterFailure() {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
