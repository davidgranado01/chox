package scsbre.engine;

import scsbre.model.ClaimStatus;
import scsbre.model.IClaimInfo;

public interface IBusinessRule {
    RuleEvaluation applyToClaim(IClaimInfo claim); 
    String getFailureMessage();
    String getRuleId();
    ClaimStatus getStatusAfterFailure();
}
