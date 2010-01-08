package idas.chox.web.actions;

import idas.chox.core.model.Filter;
import idas.chox.core.services.FilterService;
import java.util.List;

public class FilterRecordCounterAction extends BaseAction {

    private FilterService filterService;
    private List<Filter> filters;


    @Override
    public String execute() throws Exception {

        filters = filterService.getAvailableFilters(this.getAuthenticatedUser());
        return SUCCESS;
    }

    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    
}
