package idas.chox.core.bre;

import idas.chox.core.model.Claim;

public interface IBusinessRule {
    RuleEvaluation applyToClaim(Claim claim);
    String getNarrative();
    String getRuleId();
    String getStatusAfterFailure(boolean isTpiClaim);
}