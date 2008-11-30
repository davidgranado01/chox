/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.History;
import scsbre.engine.*;
import java.util.List;
import chox.model.Claim;

public interface HistoryService {
    public Boolean saveHistory(History history);
    public void logInvoiceValidationErrorMsg(RulesEngineResponse reponse, Claim claim);
    public List<History> getHistoryByClaim(Claim claim, Boolean isShowAll, Boolean isPublic);
}