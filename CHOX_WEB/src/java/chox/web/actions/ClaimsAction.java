
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.services.ClaimService;
import chox.web.viewdata.claimGridViewData;
import java.util.ArrayList;
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
    private String status;

    public ClaimsAction() {
    }

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getResults() {
        return results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getJsonData() {
        try {
            List<claimGridViewData> viewData = new ArrayList<claimGridViewData>();

            for (Object c : results) {
                viewData.add(new claimGridViewData((Claim) c));
            }

            JSONArray jsonArray = JSONArray.fromObject(viewData);
            return "{totalCount:" + this.getTotalCount() + ",results:" + jsonArray.toString() + "}";
        } catch (Exception ex) {
            return null;
        }

    //return jsonArray.toString();
    }

    public String getClaimsbyStatus() {

        if (status.equalsIgnoreCase("all")) {
            results = this.service.listAllClaims();
        } else {

            results = this.service.listClaimsByStatus(status);
        }
        totalCount = results.size();
        return SUCCESS;
    }

}