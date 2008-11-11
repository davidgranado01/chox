/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.services.ClaimService;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class ClaimsAction extends BaseAction {

    private List results;
    private int totalCount;
    private ClaimService service;

    public ClaimsAction() {
    }

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    public List getResults() {
        return results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getJsonData() {
        JSONArray jsonArray = JSONArray.fromObject(results); 
        return "{totalCount:" + this.getTotalCount() + ",results:" + jsonArray.toString() + "}";
        //return jsonArray.toString();
    }

    public String getAllClaims() {
        results = this.service.listAllClaims();
        totalCount = results.size();
        return SUCCESS;
    }
}