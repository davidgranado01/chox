package idas.chox.core.services;

import idas.chox.core.model.BreRules;
import java.util.List;

import idas.chox.core.model.History;
import idas.chox.core.model.Claim;

public interface HistoryService {


    List<History> getHistoryByClaim(int claimId, Boolean isShowAll, Boolean isPublic);
    List<BreRules> getBreRuleFailuresByClaimId(int claimId);

    void markHistoryAsOldByClaim(Claim claim);
    
}
