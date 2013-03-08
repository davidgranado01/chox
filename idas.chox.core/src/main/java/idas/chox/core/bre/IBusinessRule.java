package idas.chox.core.bre;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;

public interface IBusinessRule {
    RuleEvaluation applyToClaim(Claim claim);
    String getNarrative();
    String getRuleId();
    String getStatusAfterFailure(ClaimType claimType);
}