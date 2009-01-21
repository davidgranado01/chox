package scsbre.engine;

import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public interface IBusinessRule {
    RuleEvaluation applyToClaim(IClaimInfo claim); 
    String getNarrative();
    String getRuleId();
    ClaimStatus getStatusAfterFailure();
}
