package idas.chox.core.services;

import idas.chox.core.model.BreRules;
import java.util.List;

import idas.chox.core.model.Claim;

public interface HistoryService {


    List<BreRules> getBreRuleFailuresByClaim(Claim claim);
    void markHistoryAsOldByClaim(Claim claim);
    
}
