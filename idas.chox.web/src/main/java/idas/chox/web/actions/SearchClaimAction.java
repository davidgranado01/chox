package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Filter;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.FilterService;
import idas.chox.core.services.LookupService;
import idas.chox.web.viewdata.claimGridViewData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.util.StringHelper;

public class SearchClaimAction extends BaseAction implements ModelDriven<ClaimSearchCriteria>, Preparable, SessionAware {

    private LookupService lookupService;
    private ClaimService claimService;
    private FilterService filterService;
    private List statuses;
    private List insurers;
    private List suppliers;
    private List results;
    private int totalCount;
    private String actionResult;
    private String filterName;
    private ClaimSearchCriteria claimSearchCriteria;
    private Map session;

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
                viewData.add(new claimGridViewData(c, getAuthenticatedUser()));
            }

            JSONArray jsonArray = JSONArray.fromObject(viewData);
            return "{totalCount:" + this.getTotalCount() + ",results:" + jsonArray.toString() + "}";

        } catch (Exception ex) {
            handleException(ex);
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

        Integer start = claimSearchCriteria.getStart();
        Integer limit = claimSearchCriteria.getLimit();
        String sort = claimSearchCriteria.getSort();
        String dir = claimSearchCriteria.getDir();

        if (!StringHelper.isEmpty(filterName)) {
            Filter filter = filterService.getFilter(filterName);
            ClaimSearchCriteria filterCriteria = filter.getClaimSearchCriteria();
            mergeClaimSearchCriteria(filterCriteria);
            claimSearchCriteria = filterCriteria;
        }

        SearchResult searchResult = this.claimService.searchClaims(claimSearchCriteria, start, limit, sort, dir);
        results = searchResult.getResult();
        totalCount = searchResult.getTotalCount();
        return SUCCESS;
    }

    private void mergeClaimSearchCriteria(ClaimSearchCriteria c) {

        Integer start = claimSearchCriteria.getStart();
        Integer limit = claimSearchCriteria.getLimit();
        String sort = claimSearchCriteria.getSort();
        String dir = claimSearchCriteria.getDir();

        c.setStart(start);
        c.setLimit(limit);
        c.setSort(sort);
        c.setDir(dir);
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public ClaimSearchCriteria getModel() {
        return claimSearchCriteria;
    }

    public void prepare() throws Exception {
        if (claimSearchCriteria == null) {
            claimSearchCriteria = new ClaimSearchCriteria();
        }
    }

    @Override
    public String getActionResult() {
        return actionResult;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }

    public void setSession(Map map) {
        this.session = map;
    }
}
