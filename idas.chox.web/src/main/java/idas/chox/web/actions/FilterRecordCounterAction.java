package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Filter;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.FilterService;
import idas.chox.web.viewdata.FilterViewData;

public class FilterRecordCounterAction extends BaseAction implements ModelDriven<ClaimSearchCriteria>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(FilterRecordCounterAction.class);

    private FilterService filterService;
    private List<Filter> filters;
    private ClaimService claimService;
    private List<FilterViewData> filterViewDatas = new ArrayList<>();
    private ClaimSearchCriteria claimSearchCriteria;

    public String getJsonData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(filterViewDatas);
            } catch (JsonProcessingException ex) {
                LOG.error("Error converting filterViewDatas to json string.");
            }
            return "{totalCount:" + filterViewDatas.size() + ",results:" + jsonString + "}";
        } catch(Exception ex) {
            LOG.error("exception occurred:", ex);
            return null;
        }
       
    }

    @Override
    public String execute() throws Exception {

        filters = filterService.getAvailableFilters(this.getAuthenticatedUser());
        for (Filter filter : filters) {
            long startTime = System.currentTimeMillis();
            LOG.debug("Setting up filter '{}'", filter.getName());
            FilterViewData filterViewData = new FilterViewData();
            filterViewData.setKey(filter.getKey());
            filterViewData.setQueueDescription(filter.getQueueDescription());
//            ClaimSearchCriteria filterClaimSearchCriteria = (ClaimSearchCriteria) SerializationUtils.clone(claimSearchCriteria);
//            removeUnwantedSearchCriteria(filterClaimSearchCriteria);
            ClaimSearchCriteria filterClaimSearchCriteria = getClaimSearchCriteria();
            try {
                filterViewData.setQueueCount(claimService.countClaims(filter.getClaimSearchCriteria(filterClaimSearchCriteria)));
                filterViewData.setQueueNameWithCount(String.format("%s (%d)", filter.getName(), filterViewData.getQueueCount()));
            } catch (Exception ex) {
                // Sometime this error happens when the user logout immediately after clicking the inbox queue but before the server sends the respons. 
                // Error is thrown while getting the user information. Need to investigate further to see why it is not throwing exception all the times but some times.
                if (getAuthenticatedUser() == null) {
                    LOG.warn("Error setting up filter '{}' when authenticated user is empty: ", filter.getName());
                } else {
                    LOG.warn("Error setting up filter '{}' for user '{}': ", filter.getName(), getAuthenticatedUser().getDisplayName(), ex);
                }
            }
            LOG.debug("    filter description: '{}'", filterViewData.getQueueNameWithCount());
            filterViewData.setQueueName(filter.getName());
            filterViewData.setClaimSearchCriteria(filterClaimSearchCriteria);
            filterViewDatas.add(filterViewData);
            LOG.trace("Time taken to get the claims count for queue '{}' is {} ", filter.getName(), (System.currentTimeMillis() - startTime));
        }
        return SUCCESS;
    }

    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public List<FilterViewData> getFilterViewDatas() {
        return filterViewDatas;
    }

    @Override
    public ClaimSearchCriteria getModel() {
        return claimSearchCriteria;
    }

    @Override
    public void prepare() throws Exception {
        claimSearchCriteria = new ClaimSearchCriteria();
    }
    
//<editor-fold defaultstate="collapsed" desc="comment">
    /*public boolean isSyncWithSearchCriteria() {
    return syncWithSearchCriteria;
    }
    
    public void setSyncWithSearchCriteria(boolean syncWithSearchCriteria) {
    this.syncWithSearchCriteria = syncWithSearchCriteria;
    }
    
    private void removeUnwantedSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
    // For queue count it should not depend upon the user selected statuses.
    if (claimSearchCriteria.getStatuses() != null && !claimSearchCriteria.getStatuses().isEmpty()) {
    claimSearchCriteria.getStatuses().clear();
    }
    }*/
//</editor-fold>
    
    private ClaimSearchCriteria getClaimSearchCriteria() {
        ClaimSearchCriteria clonedClaimSearchCriteria = new ClaimSearchCriteria();
//<editor-fold defaultstate="collapsed" desc="comment">
        /*        if (syncWithSearchCriteria) {
        
        if (claimSearchCriteria.getSupplierIds() != null) {
        clonedClaimSearchCriteria.setSupplierIds(claimSearchCriteria.getSupplierIds());
        }
        
        if (claimSearchCriteria.getInsurerIds() != null) {
        clonedClaimSearchCriteria.setInsurerIds(claimSearchCriteria.getInsurerIds());
        }
        
        if (claimSearchCriteria.getClaimTypes() != null) {
        clonedClaimSearchCriteria.setClaimTypes(claimSearchCriteria.getClaimTypes());
        }
        }*/
//</editor-fold>
        return clonedClaimSearchCriteria;
    }

}
