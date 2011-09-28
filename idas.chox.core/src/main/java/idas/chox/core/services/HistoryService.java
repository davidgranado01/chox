package idas.chox.core.services;

import idas.chox.core.model.History;
import java.util.List;
import idas.chox.core.model.Claim;

public interface HistoryService {


    public List<History> getHistoryByClaim(Claim claim, Boolean isShowAll, Boolean isPublic);

    public void markHistoryAsOldByClaim(Claim claim);
    
}
