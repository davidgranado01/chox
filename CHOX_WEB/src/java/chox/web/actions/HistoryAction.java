/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.History;
import chox.services.HistoryService;
import chox.web.viewdata.HistoryViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class HistoryAction extends BaseAction {

    private List<HistoryViewData> histories;
    private HistoryService service;
    private int claimId;

    public List<HistoryViewData> getHistories() {
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
        
        return "{totalCount:" + this.histories.size() + ",results:" + jObject.toString() + "}";

    }

     /*
     * isShowAll : true > SHOW ALL RECORDS WITH TYPE IS ERROR AND INFO
     * isShowAll : false > SHOW ALL RECORDS WITH TYPE IS ERROR ONLY
     * isPublic : true > SHOW ALL RECORDS WITH IS_PUBLIC IS TRUE ONLY
     * isPublic : false > SHOW ALL RECORDS REGARDLESS THE IS_PUBLIC
     */ 
     
    @Override
    public String execute() {
    
        Boolean isShowAll = this.getIsInsurer();      
        Boolean isPublic = this.getIsCHO();

        Claim claim = new Claim();
        claim.setId(claimId);
        List<History> historiesData = this.service.getHistoryByClaimSortByDate(claim, isShowAll, isPublic);
        histories = new ArrayList<HistoryViewData>();
        for(History h : historiesData)
        {
            histories.add(new HistoryViewData(h));
        }

        return SUCCESS;
    }
}