/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.LookupService;
import idas.chox.web.viewdata.claimGridViewData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.struts2.interceptor.SessionAware;

public class SearchClaimAction extends BaseAction implements ModelDriven<ClaimSearchCriteria>, Preparable, SessionAware {

    private Map session;
    private List statuses;
    private List insurers;
    private List suppliers;
    private LookupService lookupService;
    private ClaimService claimService;
    private List results;
    private int totalCount;
    private String actionResult;
    private ClaimSearchCriteria claimSearchCriteria;

    public List getStatuses() {
        if (statuses == null) {
            statuses = this.lookupService.getStatuses();
        }
        return statuses;
    }

    public List getInsurers() {
        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }
        return insurers;
    }

    public List getSuppliers() {
        if (suppliers == null) {
            suppliers = this.lookupService.getAllSuppliers();
        }
        return suppliers;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getJsonData() {
        try {
            List<claimGridViewData> viewData = new ArrayList<claimGridViewData>();

            for (Object obj : results) {
                Claim c = (Claim) obj;
                viewData.add(new claimGridViewData(c, getAuthenticatedUser().getUser()));
            }

            JSONArray jsonArray = JSONArray.fromObject(viewData);
            return "{totalCount:" + this.getTotalCount() + ",results:" + jsonArray.toString() + "}";

        } catch (Exception ex) {
            return null;
        }
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public void setClaimService(ClaimService service) {
        this.claimService = service;
    }

    public String doSearchClaim() throws Exception {

        session.put("searchCriteria", claimSearchCriteria);
        Integer start = claimSearchCriteria.getStart();
        Integer limit = claimSearchCriteria.getLimit();
        String sort = claimSearchCriteria.getSort();
        String dir = claimSearchCriteria.getDir();
        SearchResult searchResult = this.claimService.searchClaims(claimSearchCriteria, start, limit, sort, dir);
        results = searchResult.getResult();
        totalCount = searchResult.getTotalCount();
        return SUCCESS;

    }

    public String getPageIndexOfCurrentSearch() {

        actionResult = "-1";
        if (session.containsKey("searchCriteria")) {
            if (claimSearchCriteria.getIsSearched()) {
                Integer start = claimSearchCriteria.getStart();
                actionResult = start.toString();
            }
        }

        return SUCCESS;
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public void setSession(Map session) {
        this.session = session;
    }

    public ClaimSearchCriteria getModel() {
        return claimSearchCriteria;
    }

    public void prepare() throws Exception {
        if (claimSearchCriteria == null) {
            if (session != null && session.containsKey("searchCriteria")) {
                claimSearchCriteria = (ClaimSearchCriteria) session.get("searchCriteria");
            } else {
                claimSearchCriteria = new ClaimSearchCriteria();
            }
        }
    }

    public String getActionResult() {
        return actionResult;
    }
}
