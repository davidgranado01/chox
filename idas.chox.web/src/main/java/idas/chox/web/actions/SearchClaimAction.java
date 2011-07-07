package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Filter;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LookupItem;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.FilterService;
import idas.chox.core.services.LookupService;
import idas.chox.service.claim.ClaimObjectService;
import idas.chox.web.viewdata.ClaimGridViewData;

public class SearchClaimAction extends BaseAction implements ModelDriven<ClaimSearchCriteria>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(SearchClaimAction.class);
    private LookupService lookupService;
    private ClaimService claimService;
    private FilterService filterService;
    private ClaimObjectService claimObjectService;
    private List<LookupItem> statuses;
    private List<LookupItem> liabilityStatuses;
    private List<Insurer> insurers;
    private List<Chorganisation> suppliers;
    private List<Object> results;
    private int totalCount;
    private String actionResult;
    private String filterName;
    private ClaimSearchCriteria claimSearchCriteria;
    private boolean canLoadData=true;
    
    public boolean isCanLoadData() {
        return canLoadData;
    }

    public void setCanLoadData(boolean canLoadData) {
        this.canLoadData = canLoadData;
    }

    public List getStatuses() {
        if (statuses == null) {
            statuses = this.lookupService.getStatuses(getInsurerIsWorkgroupEnabled(), getInsurerIsClaimOwnershipEnabled(),
                    getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), getIsTpiEnabledEnabled());
        }
        return statuses;
    }

    public List getLiabilityStatuses() {
        if (liabilityStatuses == null) {
            liabilityStatuses = this.lookupService.getLiabilityStatuses();
        }
        return liabilityStatuses;
    }

    public String getStatusesJsonString() {
        String statusesJson = JSONArray.fromObject(getStatuses()).toString();
        return "{totalCount:" + statuses.size() + ", results:" + statusesJson + "}";
    }

    public String getLiabilityStatusesJsonString() {
        String liabilityStatusesJson = JSONArray.fromObject(getLiabilityStatuses()).toString();
        return "{totalCount:" + liabilityStatuses.size() + ", results:" + liabilityStatusesJson + "}";
    }

    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getInsurers().size());
        for (Insurer insurer : insurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
//           System.out.println("Insurers json is :" + JSONArray.fromObject(luItems).toString());
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }

    public String getSuppliersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getSuppliers().size());
        for (Chorganisation supplier : suppliers) {
            luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
        }
//           System.out.println("Insurers json is :" + JSONArray.fromObject(luItems).toString());
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
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

    public Map getLiabilityStatusDropDownMap() {
        return claimObjectService.getLiabilityStatusMap();
    }

    public Map getLiabilityStatusDropDownSearchMap() {
        return claimObjectService.getLiabilityStatusSearchMap();
    }

    public int getTotalCount() {
        return totalCount;
    }

    public String getJsonError() {
        return "{status: 'error', message: 'Illegal operation detected: you have been logged out'";
    }

    public String getJsonData() {

        try {
            LOG.debug("Converting results to view data");
            List<ClaimGridViewData> viewData = new ArrayList<ClaimGridViewData>();

            for (Object obj : results) {
                Claim c = (Claim) obj;
                LOG.debug("Adding claim to view data: {}", c.getChoReference());
                viewData.add(new ClaimGridViewData(c, getAuthenticatedUser()));
            }

            JSONArray jsonArray = JSONArray.fromObject(viewData);
            return "{totalCount:" + this.getTotalCount() + ",results:" + jsonArray.toString() + "}";

        } catch (Exception ex) {
            LOG.error("Exception converting results to view data: {}", ex.getMessage());
//            ex.printStackTrace();
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

        if (canLoadData) {
            LOG.debug("In doSearchClaim().");
            Integer start = claimSearchCriteria.getStart();
            Integer limit = claimSearchCriteria.getLimit();
            String sort = claimSearchCriteria.getSort();
            String dir = claimSearchCriteria.getDir();

            if (!claimSearchCriteria.validate()) {
                LOG.warn("Claim search criteria are invalid.");
                this.getActionResponse().AddError("Please check your search string.");
                /*
                 * there is no workaround at the moment to inform user about invalid search criteria as Extjs store do not listen to custom json response in dataStore.
                 * anyway the search result will be empty and no error or exception is thrown. 
                 */
                results = new ArrayList<Object>();
                return SUCCESS;
            }
            getSession().put("searchCriteria", claimSearchCriteria);

            if (!StringHelper.isEmpty(filterName)) {
                Filter filter = filterService.getFilter(filterName);
                ClaimSearchCriteria filterCriteria = filter.getClaimSearchCriteria();
                mergeClaimSearchCriteria(filterCriteria);
                claimSearchCriteria = filterCriteria;
            }

            getSession().put("searchReportCriteria", null);
            getSession().put("searchReportCriteria", claimSearchCriteria);

            LOG.debug("Calling search claim service");
            SearchResult searchResult = this.claimService.searchClaims(claimSearchCriteria, start, limit, sort, dir);
            LOG.debug("Search claim service retrieved {} results", searchResult.getTotalCount());

            results = searchResult.getResult();
            totalCount = searchResult.getTotalCount();
            LOG.debug("Returning SUCCESS from doSearchClaim() action");
            return SUCCESS;
        } else {
            results = new ArrayList<Object>();
            return SUCCESS;
        }
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

    @Override
    public ClaimSearchCriteria getModel() {
        return claimSearchCriteria;
    }

    @Override
    public void prepare() throws Exception {

        if (claimSearchCriteria == null) {
            if (getSession() != null && getSession().containsKey("searchCriteria")) {
                claimSearchCriteria = (ClaimSearchCriteria) getSession().get("searchCriteria");
            } else {
                claimSearchCriteria = new ClaimSearchCriteria();
            }
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

  
    public ClaimObjectService getClaimObjectService() {
        return claimObjectService;
    }

    public void setClaimObjectService(ClaimObjectService claimObjectService) {
        this.claimObjectService = claimObjectService;
    }
}
