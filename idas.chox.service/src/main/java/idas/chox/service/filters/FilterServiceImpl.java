package idas.chox.service.filters;

import idas.chox.core.services.FilterService;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.List;
import idas.chox.core.model.Filter;
import idas.chox.core.model.WebUser;
import java.util.ArrayList;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class FilterServiceImpl implements FilterService, BeanFactoryAware {

    private List<Filter> availableFilters;
    private BeanFactory beanFactory;   
    private ApplicationAccessibility applicationAccessibility;

    @Override
    public List<Filter> getAvailableFilters(WebUser webUser) {

        List<Filter> filters = new ArrayList<Filter>();

        if (availableFilters != null) {
            if (webUser != null) {
                for (Filter filter : availableFilters) {
                    if (applicationAccessibility.checkFilterAccessibility(filter.getKey(), webUser.getRoles()) > 0) {
                        filters.add(filter);
                    }
                }
            } else {
                filters.addAll(availableFilters);
            }
        }
        return filters;
    }

    @Override
    public void setAvailableFilters(List<Filter> availableFilters) {
        this.availableFilters = availableFilters;
    }

    @Override
    public Filter getFilter(String filterName) throws Exception {

        Filter filter = (Filter) beanFactory.getBean(filterName);
        if (filter == null) {
            throw new Exception("Invalid filter name");
        }
        return filter;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }
}
