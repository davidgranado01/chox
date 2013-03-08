package idas.chox.service.bre.rules;

import idas.chox.core.bre.IBusinessRule;
import idas.chox.core.bre.RuleEvaluation;
import idas.chox.core.bre.RuleEvaluationResult;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Customer;

public class ValidateUniqueVehicleRegistrationNumber implements IBusinessRule {

    private String narrative = "";

    @Override
    public RuleEvaluation applyToClaim(Claim claim) {

        RuleEvaluation res = new RuleEvaluation();
        res.setIsVisibleToCHO(false);
        res.setRelatedRule(this);
        res.setClaimType(claim.getClaimType());

        if (claim.getBreBand().isValidateUniqueVehicleRegistrationNumber()) {

            Customer Icust = claim.getCustomer();

            Boolean isVehicleRegistrationExist = Icust.isVehicleRegistrationExist();

            res.setResult(isVehicleRegistrationExist ? RuleEvaluationResult.RULE_FAILED : RuleEvaluationResult.RULE_PASSED);

            if (isVehicleRegistrationExist) {
                narrative = "The Customer's Vehicle Registration Number supplied already exists in the system.";
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
        return "021";
    }

    @Override
    public String getStatusAfterFailure(ClaimType claimType) {
        return ClaimStatus.INVOICE_ESCALATED_TO_CH;
    }
}
