/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.History;
import chox.services.HistoryService;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class HistoryAction extends BaseAction {

    private List<History> histories;
    private HistoryService service;
    private int claimId;

    public List<History> getHistories() {
        return histories;
    }

    public void setClaimId(int claimId) {
        this.claimId = claimId;
    }
    
    public void setHistoryService(HistoryService service)
    {
        this.service = service;
    }
    
     public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.histories);
        return jObject.toString();
    }

    @Override
    public String execute() {

        Claim claim = new Claim();
        claim.setId(claimId);
        histories = this.service.getHistoryByClaim(claim);

        return SUCCESS;
    }
}