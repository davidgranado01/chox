package scsbre.model;

import scsbre.engine.BusinessRule;
import scsbre.engine.RuleEvaluationResult;

public class HasAllowedVehicleClassRule implements BusinessRule {

    public RuleEvaluationResult run(IClaimInfo claim) {


        RuleEvaluationResult result = new RuleEvaluationResult();
        boolean success = claim.getHireDetail().getVClass().getPrice().compareTo(claim.getVClass().getPrice()) <= 0;
        result.setWasPassed(success);
        String msg = success ? "" : "Invalid Vehicle Class";
        result.setMessage(msg);
        result.setIsVisibleToCHO(true);
        return result;
    }
}
