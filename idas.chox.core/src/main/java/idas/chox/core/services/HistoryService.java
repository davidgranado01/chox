package idas.chox.core.services;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.History;
import java.util.List;
import idas.chox.core.model.Claim;

public interface HistoryService {

    public Boolean saveHistory(History history);

    public void logInvoiceValidationErrorMsg(RulesEngineResponse reponse, Claim claim);

    public List<History> getHistoryByClaim(Claim claim, Boolean isShowAll, Boolean isPublic);

    public void saveHistories(List<History> histories);
}
