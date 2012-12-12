package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import idas.chox.core.model.*;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.FilterService;
import idas.chox.core.services.LookupService;
import idas.chox.web.viewdata.ClaimGridViewData;

public class SearchClaimAction extends BaseAction implements ModelDriven<ClaimSearchCriteria>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(SearchClaimAction.class);
    private LookupService lookupService;
    private ClaimService claimService;
    private FilterService filterService;
    private List<LookupItem> claimStatusesLookupItem;
    private List<LookupItem> claimTypesLookupItem;
    private List<LookupItem> liabilityStatusesLookupItem;
    private List<Insurer> insurers;
    private List<Chorganisation> suppliers;
    private List<Object> results;
    private int totalCount;
    private String actionResult;
    private String filterName;
    private int filterOrgId;
    private int filterClaimTypeId = -1;
    private ClaimSearchCriteria claimSearchCriteria;
    private boolean canLoadData = true;
    private boolean inbox;

    public boolean isCanLoadData() {
        return canLoadData;
    }

    public void setCanLoadData(boolean canLoadData) {
        this.canLoadData = canLoadData;
    }

    public List<LookupItem> getClaimStatusesAsLookupItem() {
        if (claimStatusesLookupItem == null) {
            claimStatusesLookupItem = this.lookupService.getStatuses(getInsurerIsWorkgroupEnabled(),
                                    getInsurerIsClaimOwnershipEnabled(), getInsurerIsFnolEnabled(),
                                    getInsurerIsEngineersEnabled(), getIsTpiEnabledEnabled(),
                                    getInsurerIsUploadEnabled(), getIsSubscriberEnabled());
        }
        return claimStatusesLookupItem;
    }
    
    /*
     * Please note this method will return only Claim statuses from the
     * loaded(model) claimSearchCriteria and not from available Claim statuses.
     */
    public String getClaimStatusesAsString() {

        if (claimSearchCriteria.getStatuses() != null) {
            StringBuilder returnString = new StringBuilder();
            for (String c : claimSearchCriteria.getStatuses()) {
                returnString.append(c).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

    /*
     * Please note this method will return only Insurer Ids from the
     * loaded(model) claimSearchCriteria and not from available Insurer Id.
     */
    public String getInsurerIdsAsString() {

        if (claimSearchCriteria.getInsurerIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : claimSearchCriteria.getInsurerIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

    /*
     * Please note this method will return only Supplier Ids from the
     * loaded(model) claimSearchCriteria and not from available Supplier Id.
     */
    public String getSupplierIdsAsString() {

        if (claimSearchCriteria.getSupplierIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : claimSearchCriteria.getSupplierIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

    /*
     * Please note this method will return only Workgroup Ids from the
     * loaded(model) claimSearchCriteria and not from available Workgroup Id.
     */
    public String getWorkgroupIdsAsString() {

        if (claimSearchCriteria.getWorkgroupIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : claimSearchCriteria.getWorkgroupIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    /*
     * Please note this method will return only Supplier Claim owner Ids from the
     * loaded(model) claimSearchCriteria and not from available Supplier Claim owner Id.
     */
    public String getSupplierClaimOwnerIdsAsString() {

        if (claimSearchCriteria.getSupplierClaimOwnerIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : claimSearchCriteria.getSupplierClaimOwnerIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
     /*
     * Please note this method will return only Supplier Claim owner Ids from the
     * loaded(model) claimSearchCriteria and not from available Supplier Claim owner Id.
     */
    public String getClaimOwnerIdsAsString() {

        if (claimSearchCriteria.getClaimOwnerIds() != null) {
            StringBuilder returnString = new StringBuilder();
            for (Integer i : claimSearchCriteria.getClaimOwnerIds()) {
                returnString.append(i.toString()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    public List<LookupItem> getClaimTypesAsLookupItem() {
        if (claimTypesLookupItem == null) {
            claimTypesLookupItem = this.lookupService.getClaimTypes();
        }
        return claimTypesLookupItem;
    }

    /*
     * Please note this method will return only Liability value from the
     * loaded(model) claimSearchCriteria and not from available LiabilityStatus.
     */
    public String getClaimTypesValueAsString() {

        if (claimSearchCriteria.getClaimTypes() != null) {
            StringBuilder returnString = new StringBuilder();
            for (ClaimType c : claimSearchCriteria.getClaimTypes()) {
                returnString.append(c.getClaimTypeValue()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }
    
    public List<LookupItem> getLiabilityStatusesAsLookupItem() {
        return getLiabilityStatusesAsLookupItem(false);
    }
    
    public List<LookupItem> getLiabilityStatusesAsLookupItem(boolean withNull) {
        if (liabilityStatusesLookupItem == null) {
            liabilityStatusesLookupItem = this.lookupService.getLiabilityStatuses(withNull);
        }
        return liabilityStatusesLookupItem;
    }
    
    /*
     * Please note this method will return only Liability value from the
     * loaded(model) claimSearchCriteria and not from available LiabilityStatus.
     */
    public String getLiabilityStatusesValueAsString() {

        if (claimSearchCriteria.getLiabilityStatuses() != null) {
            StringBuilder returnString = new StringBuilder();
            for (LiabilityStatus s : claimSearchCriteria.getLiabilityStatuses()) {
                returnString.append(s.getLiablityValue()).append(",");
            }
            return returnString.toString().substring(0, returnString.length() - 1);
        }
        return null;
    }

    public String getStatusesJsonString() {
        String statusesJson = JSONArray.fromObject(getClaimStatusesAsLookupItem()).toString();
        return "{totalCount:" + claimStatusesLookupItem.size() + ", results:" + statusesJson + "}";
    }

    public String getClaimTypesJsonString() {
        String claimTypesJson = JSONArray.fromObject(getClaimTypesAsLookupItem()).toString();
        return "{totalCount:" + claimTypesLookupItem.size() + ", results:" + claimTypesJson + "}";
    }

    public String getLiabilityStatusesJsonString() {
        String liabilityStatusesJson = JSONArray.fromObject(getLiabilityStatusesAsLookupItem(false)).toString();
        return "{totalCount:" + liabilityStatusesLookupItem.size() + ", results:" + liabilityStatusesJson + "}";
    }

    public String getLiabilityStatusesJsonStringWithNull() {
        String liabilityStatusesJson = JSONArray.fromObject(getLiabilityStatusesAsLookupItem(true)).toString();
        return "{totalCount:" + liabilityStatusesLookupItem.size() + ", results:" + liabilityStatusesJson + "}";
    }

    public String getInsurersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getInsurers().size());
        for (Insurer insurer : insurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
        return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }

    public String getSuppliersJsonString() {
        List<LookupItem> luItems = new ArrayList<LookupItem>(getSuppliers().size());
        for (Chorganisation supplier : suppliers) {
            luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
        }
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
            
            filterName = getFilterName();
            filterOrgId = getFilterOrgId();
            filterClaimTypeId = getFilterClaimTypeId();
            if (!StringHelper.isEmpty(filterName)) {
                Filter filter = filterService.getFilter(filterName);
                ClaimSearchCriteria filterCriteria;
                if (this.getIsCHO()) {
                    LOG.debug("Filtering on insurerId={}", filterOrgId);
                    filterCriteria = filter.getClaimSearchCriteria(Boolean.TRUE, filterOrgId, -1, filterClaimTypeId);
                } else if (this.getIsInsurer()) {
                    LOG.debug("Filtering on choId={}", filterOrgId);
                    filterCriteria = filter.getClaimSearchCriteria(Boolean.FALSE, -1, filterOrgId, filterClaimTypeId);
                } else {
                    filterCriteria = filter.getClaimSearchCriteria(null, -1, -1, filterClaimTypeId);
                    LOG.debug("No search filter on organisation");
                }

                mergeClaimSearchCriteria(filterCriteria);
                claimSearchCriteria = filterCriteria;
            }

            getSession().put("searchReportCriteria", null); 
            getSession().put("searchReportCriteria", claimSearchCriteria);

            LOG.debug("Calling search claim service");
            if (claimSearchCriteria == null) {
                LOG.debug("Claim search criteria is null.");
                results = new ArrayList<Object>();
                return SUCCESS;
            }
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

        if(c != null){
            Integer start = claimSearchCriteria.getStart();
            Integer limit = claimSearchCriteria.getLimit();
            String sort = claimSearchCriteria.getSort();
            String dir = claimSearchCriteria.getDir();
    
            c.setStart(start);
            c.setLimit(limit);
            c.setSort(sort);
            c.setDir(dir);
        }
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
        if (getSession().containsKey("filterKey") && inbox) {
            return (String) getSession().get("filterKey");
        } else {
            return filterName;
        }
    }

    public int getFilterOrgId() {
        if (getSession().containsKey("filterOrgId") && inbox) {
            return (Integer) getSession().get("filterOrgId");
        } else {
            return filterOrgId;
        }
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public void setFilterOrgId(int filterOrgId) {
        this.filterOrgId = filterOrgId;
    }

    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }

    public int getFilterClaimTypeId() {
        if (getSession().containsKey("filterClaimTypeId") && inbox) {
            return (Integer) getSession().get("filterClaimTypeId");
        } else {
            return filterClaimTypeId;
        }
    }

    public void setFilterClaimTypeId(int filterClaimTypeId) {
        this.filterClaimTypeId = filterClaimTypeId;
    }

    public boolean isInbox() {
        return inbox;
    }

    public void setInbox(boolean inbox) {
        this.inbox = inbox;
    }
    
}
