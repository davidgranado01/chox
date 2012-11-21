package idas.chox.web.actions;

import idas.chox.core.model.Filter;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.FilterService;
import idas.chox.web.viewdata.FilterViewData;
import java.util.ArrayList;
import java.util.List;

public class FilterRecordCounterAction extends BaseAction {

    private FilterService filterService;
    private List<Filter> filters;
    private ClaimService claimService;
    private List<FilterViewData> filterViewDatas = new ArrayList<FilterViewData>();
    private int filterOrgId = -1;
    private int filterClaimTypeId = -1;

    @Override
    public String execute() throws Exception {

        filters = filterService.getAvailableFilters(this.getAuthenticatedUser());
        for (Filter filter : filters) {

            //            filter.setCount(claimService.countClaims(filter.getClaimSearchCriteria())); removed for bug#964
            
            FilterViewData filterViewData = new FilterViewData();
            filterViewData.setKey(filter.getKey());
            if (getIsCHO())
                filterViewData.setDescription(String.format("%s (%d)", filter.getName(), claimService.countClaims(filter.getClaimSearchCriteria(Boolean.TRUE, filterOrgId, -1, filterClaimTypeId)).intValue()));
            else if (getIsInsurer())
                filterViewData.setDescription(String.format("%s (%d)", filter.getName(), claimService.countClaims(filter.getClaimSearchCriteria(Boolean.FALSE, -1, filterOrgId, filterClaimTypeId)).intValue()));
            else
                filterViewData.setDescription(String.format("%s (%d)", filter.getName(), claimService.countClaims(filter.getClaimSearchCriteria(null, -1, -1, filterClaimTypeId)).intValue()));
            filterViewData.setGridTitle(filter.getName());
            filterViewDatas.add(filterViewData);
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

    public int getFilterOrgId() {
        return filterOrgId;
    }

    public void setFilterOrgId(int filterOrgId) {
        this.filterOrgId = filterOrgId;
    }

    public int getFilterClaimTypeId() {
        return filterClaimTypeId;
    }

    public void setFilterClaimTypeId(int filterClaimTypeId) {
        this.filterClaimTypeId = filterClaimTypeId;
    }

}
