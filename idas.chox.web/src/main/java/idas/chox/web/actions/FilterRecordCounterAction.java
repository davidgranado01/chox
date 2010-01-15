package idas.chox.web.actions;

import idas.chox.core.model.Filter;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.FilterService;
import java.util.List;

public class FilterRecordCounterAction extends BaseAction {

    private FilterService filterService;
    private List<Filter> filters;
    private ClaimService claimService;

    @Override
    public String execute() throws Exception {

        filters = filterService.getAvailableFilters(this.getAuthenticatedUser());
        for(Filter filter : filters)
        {
            filter.setCount(claimService.countClaims(filter.getClaimSearchCriteria()));
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
}
