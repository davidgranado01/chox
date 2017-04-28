package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private List<LookupItem> finalReviewValuesLookupItem;
    private List<Insurer> insurers;
    private List<Chorganisation> suppliers;
    private List<Object> results;
    private List<Filter> filters;
    private int totalCount;
    private String actionResult;
    private ClaimSearchCriteria claimSearchCriteria;
    private boolean canLoadData = true;
    private boolean loadSearchPanelSelectionFromSession;

    public boolean isLoadSearchPanelSelectionFromSession() {
        return loadSearchPanelSelectionFromSession;
    }

    public void setLoadSearchPanelSelectionFromSession(boolean loadSearchPanelSelectionFromSession) {
        this.loadSearchPanelSelectionFromSession = loadSearchPanelSelectionFromSession;
    }

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
                                    isInsurerUploadEnabled(), getIsSubscriberEnabled());
        }
        return claimStatusesLookupItem;
    }
    
 
    public List<LookupItem> getClaimTypesAsLookupItem() {
        if (claimTypesLookupItem == null) {
            claimTypesLookupItem = this.lookupService.getClaimTypes(getAuthenticatedUser());
        }
        return claimTypesLookupItem;
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
    
    public List<LookupItem> getFinalReviewValuesAsLookupItem() {
        if (finalReviewValuesLookupItem == null) {
            finalReviewValuesLookupItem = this.lookupService.getFinalReviewValues();
        }
        return finalReviewValuesLookupItem;
    }
    
    public String getFinalReviewValuesJsonString() {
        String finalReviewValuesJson = JSONArray.fromObject(getFinalReviewValuesAsLookupItem()).toString();
        return "{totalCount:" + finalReviewValuesLookupItem.size() + ", results:" + finalReviewValuesJson + "}";
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
        List<LookupItem> luItems = new ArrayList<>(getInsurers().size());
        for (Insurer insurer : insurers) {
            luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }

    public String getSuppliersJsonString() {
        List<LookupItem> luItems = new ArrayList<>(getSuppliers().size());
        for (Chorganisation supplier : suppliers) {
            luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
        }
        return StringEscapeUtils.escapeEcmaScript("{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}");
    }

    public boolean isAnomaliesCheckBoxVisible() {
        boolean isVisible = false;
        try {
            Filter anomoliesFilter = filterService.getFilter("HireUpdateAnomalies");
            for (Filter filter : getAvailableFilters()) {
                if (filter.getKey().equals(anomoliesFilter.getKey())) {
                    isVisible = true;
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while retrieving the HireUpdateAnomalies filter ", ex);
        }
        return isVisible;
    }
    
    public boolean isPaymentDisputeCheckBoxVisible() {
        boolean isVisible = false;
        try {
            Filter anomoliesFilter = filterService.getFilter("InvoicePaymentDispute");
            for (Filter filter : getAvailableFilters()) {
                if (filter.getKey().equals(anomoliesFilter.getKey())) {
                    isVisible = true;
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while retrieving the HireUpdateAnomalies filter ", ex);
        }
        return isVisible;
    }
    
    public boolean isEscalatedToSupervisorCheckBoxVisible() {
        boolean isVisible = false;
        try {
            Filter escalatedToSupervisorFilter = filterService.getFilter("EscalatedInvoicesToSupervisor");
            for (Filter filter : getAvailableFilters()) {
                if (filter.getKey().equals(escalatedToSupervisorFilter.getKey())) {
                    isVisible = true;
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while retrieving the EscalatedInvoicesToSupervisor filter ", ex);
        }
        return isVisible;
    }
    
    public boolean isClaimMatchActive() {
        return this.getModel().getClaimMatchValue() == null ? false :  this.getModel().getClaimMatchValue() > 0;
    }

    public boolean isMatchedClaimsCheckBoxVisible() {
        boolean isVisible = false;
        try {
            Filter matchedClaims = filterService.getFilter("MatchedClaims");
            for (Filter filter : getAvailableFilters()) {
                if (filter.getKey().equals(matchedClaims.getKey())) {
                    isVisible = true;
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while retrieving the MatchedClaims filter ", ex);
        }
        return isVisible;
    }
    
    public boolean isLiabilityStatusUpdateNotificationCheckBoxVisible() {
        boolean isVisible = false;
        try {
            Filter liabilityUpdatedFilter = filterService.getFilter("LiabilityUpdate");
            for (Filter filter : getAvailableFilters()) {
                if (filter.getKey().equals(liabilityUpdatedFilter.getKey())) {
                    isVisible = true;
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while retrieving the LiabilityUpdate filter ", ex);
        }
        return isVisible;
    }
    
    public boolean isCaseWithClientsSolicitorCheckBoxVisible() {
        boolean isVisible = false;
        try {
            for (Filter filter : getAvailableFilters()) {
                if (filter.getKey().equals(Filter.FILTER_CASE_WITH_SOLICITOR)) {
                    isVisible = true;
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while retrieving the CaseWithClientsSolicitor filter ", ex);
        }
        return isVisible;
    }
    
    public boolean isPenaltyChargesToBeAppliedCheckBoxVisible() {
        boolean isVisible = false;
        try {
            if (getIsInsurer() && isInsurerUploadEnabled()) {
                isVisible = true;
            } else {
                Filter penaltyChargesAppliedFilter = filterService.getFilter("PenaltyChargesApplied");
                for (Filter filter : getAvailableFilters()) {
                    if (filter.getKey().equals(penaltyChargesAppliedFilter.getKey())) {
                        isVisible = true;
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while retrieving the PenaltyChargesApplied filter ", ex);
        }
        return isVisible;
    }
    
    public boolean isInterimPaymentMadeCheckBoxVisible() {
        boolean isVisible = false;
        try {
            Filter interimPaymentFilter = filterService.getFilter("InterimPayment");
            for (Filter filter : getAvailableFilters()) {
                if (filter.getKey().equals(interimPaymentFilter.getKey())) {
                    isVisible = true;
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception while retrieving the InterimPayment filter ", ex);
        }
        return isVisible;
    }
    
    public List<Insurer> getInsurers() {
        if (insurers == null) {
            if (getIsInsurer()) {
                insurers = new ArrayList<>();
            } else {
                insurers = this.lookupService.getInsurers();
            }
        }
        return insurers;
    }

    public List<Chorganisation> getSuppliers() {
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
            List<ClaimGridViewData> viewData = new ArrayList<>();

            for (Object obj : results) {
                Claim c = (Claim) obj;
                LOG.debug("Adding claim to view data: {}", c.getChoReference());
                viewData.add(new ClaimGridViewData(c, getAuthenticatedUser()));
            }

            JSONArray jsonArray = JSONArray.fromObject(viewData);
            return "{totalCount:" + this.getTotalCount() + ",results:" + jsonArray.toString() + "}";

        } catch (Exception ex) {
            LOG.error("Exception converting results to view data: {}", ex.getMessage(), ex);
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
            
            if (claimSearchCriteria.getSort() == null || claimSearchCriteria.getSort().isEmpty()) {
                claimSearchCriteria.setSort("created");
            }
            
            if (claimSearchCriteria.getDir() == null || claimSearchCriteria.getDir().isEmpty()) {
                claimSearchCriteria.setDir("desc");
            }

            if (!claimSearchCriteria.validate()) {
                LOG.warn("Claim search criteria are invalid.");
                this.getActionResponse().AddError("Please check your search string.");
                /*
                 * there is no workaround at the moment to inform user about invalid search criteria as Extjs store do not listen to custom json response in dataStore.
                 * anyway the search result will be empty and no error or exception is thrown. 
                 */
                results = new ArrayList<>();
                return SUCCESS;
            }
            
            if (claimSearchCriteria.getFilterName() != null && !StringHelper.isEmpty(claimSearchCriteria.getFilterName())) {
                Filter filter = filterService.getFilter(claimSearchCriteria.getFilterName());
                filter.getClaimSearchCriteria(claimSearchCriteria);
            }

            synchronized (getSessionLock()) {
                getSession().put("searchCriteria", claimSearchCriteria);
            }
           
            LOG.debug("Calling search claim service");
            if (claimSearchCriteria == null) {
                LOG.debug("Claim search criteria is null.");
                results = new ArrayList<>();
                return SUCCESS;
            }
            SearchResult searchResult = this.claimService.searchClaims(claimSearchCriteria);
            LOG.debug("Search claim service retrieved {} results", searchResult.getTotalCount());

            results = searchResult.getResult();
            totalCount = searchResult.getTotalCount();
            LOG.debug("Returning SUCCESS from doSearchClaim() action");
            return SUCCESS;
        } else {
            synchronized (getSessionLock()) {
                getSession().put("searchCriteria", null);
            }
            results = new ArrayList<>();
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
            if (loadSearchPanelSelectionFromSession && getSession() != null && getSession().containsKey("searchCriteria")) {
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
    
    private List<Filter> getAvailableFilters() {
        if (filters == null) {
            filters = filterService.getAvailableFilters(this.getAuthenticatedUser());
        }
        return filters;
    }

    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }
    
}
