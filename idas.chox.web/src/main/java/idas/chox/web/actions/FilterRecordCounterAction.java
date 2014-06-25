package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Filter;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.FilterService;
import idas.chox.web.viewdata.FilterViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
//import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterRecordCounterAction extends BaseAction implements ModelDriven<ClaimSearchCriteria>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(FilterRecordCounterAction.class);

    private FilterService filterService;
    private List<Filter> filters;
    private ClaimService claimService;
    private List<FilterViewData> filterViewDatas = new ArrayList<FilterViewData>();
//    private int filterOrgId = -1;
//    private int filterClaimTypeId = -1;
    private String filterName;
    private ClaimSearchCriteria claimSearchCriteria;
    private boolean syncWithSearchCriteria = true;

    public String getJsonData() {
        try {
            JSONArray jObject = JSONArray.fromObject(filterViewDatas);
            return "{totalCount:" + filterViewDatas.size() + ",results:" + jObject.toString() + "}";
        } catch(Exception ex) {
            LOG.error("exception occured:", ex);
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
//            ClaimSearchCriteria filterClaimSearchCriteria = (ClaimSearchCriteria) SerializationUtils.clone(claimSearchCriteria);
//            removeUnwantedSearchCriteria(filterClaimSearchCriteria);
            ClaimSearchCriteria filterClaimSearchCriteria = getClaimSearchCriteria();
            try {
                filterViewData.setQueueClaimsCount(claimService.countClaims(filter.getClaimSearchCriteria(filterClaimSearchCriteria)));
                filterViewData.setDescription(String.format("%s (%d)", filter.getName(), filterViewData.getQueueClaimsCount()));
            } catch (Exception ex) {
                LOG.error("Error setting up filter '{}': ", filter.getName(), ex);
            }
            LOG.debug("    filter description: '{}'", filterViewData.getDescription());
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

//    public int getFilterOrgId() {
//        return filterOrgId;
//    }
//
//    public void setFilterOrgId(int filterOrgId) {
//        this.filterOrgId = filterOrgId;
//    }
//    public int getFilterClaimTypeId() {
//        return filterClaimTypeId;
//    }
//
//    public void setFilterClaimTypeId(int filterClaimTypeId) {
//        this.filterClaimTypeId = filterClaimTypeId;
//    }
    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public ClaimSearchCriteria getModel() {
        return claimSearchCriteria;
    }

    public boolean isSyncWithSearchCriteria() {
        return syncWithSearchCriteria;
    }

    public void setSyncWithSearchCriteria(boolean syncWithSearchCriteria) {
        this.syncWithSearchCriteria = syncWithSearchCriteria;
    }

    @Override
    public void prepare() throws Exception {
        claimSearchCriteria = new ClaimSearchCriteria();
    }
    
//    private void removeUnwantedSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
//        // For queue count it should not depend upon the user selected statuses.
//        if (claimSearchCriteria.getStatuses() != null && !claimSearchCriteria.getStatuses().isEmpty()) {
//            claimSearchCriteria.getStatuses().clear();
//        }
//    }
    
    private ClaimSearchCriteria getClaimSearchCriteria() {
        ClaimSearchCriteria clonedClaimSearchCriteria = new ClaimSearchCriteria();
        if (syncWithSearchCriteria) {

            if (claimSearchCriteria.getSupplierIds() != null) {
                clonedClaimSearchCriteria.setSupplierIds(claimSearchCriteria.getSupplierIds());
            }

            if (claimSearchCriteria.getInsurerIds() != null) {
                clonedClaimSearchCriteria.setInsurerIds(claimSearchCriteria.getInsurerIds());
            }

            if (claimSearchCriteria.getClaimTypes() != null) {
                clonedClaimSearchCriteria.setClaimTypes(claimSearchCriteria.getClaimTypes());
            }
        }
        return clonedClaimSearchCriteria;
    }

}
