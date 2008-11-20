package scsbre.engine;

import scsbre.model.IClaimInfo;

public interface BusinessRule {

    RuleEvaluationResult run(IClaimInfo claim);
}
