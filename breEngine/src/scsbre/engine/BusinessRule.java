package scsbre.engine;

import scsbre.model.IClaimInfo;

public abstract interface BusinessRule {

    RuleEvaluationResult run(IClaimInfo claim); 
}
