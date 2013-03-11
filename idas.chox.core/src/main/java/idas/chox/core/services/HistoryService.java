package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.History;
import idas.chox.core.model.Claim;

public interface HistoryService {


    List<History> getHistoryByClaim(Claim claim, Boolean isShowAll, Boolean isPublic);

    void markHistoryAsOldByClaim(Claim claim);
    
}
