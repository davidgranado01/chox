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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterServiceImpl implements FilterService, BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(FilterServiceImpl.class);
    private List<Filter> availableFilters;
    private BeanFactory beanFactory;
    private ApplicationAccessibility applicationAccessibility;

    @Override
    public List<Filter> getAvailableFilters(WebUser webUser) {

        List<Filter> filters = new ArrayList<Filter>();

        if (availableFilters != null) {
            if (webUser != null) {
                for (Filter filter : availableFilters) {
                    if (applicationAccessibility.checkFilterAccessibility(filter.getKey(), webUser) > 0) {
                        if (!filter.getIsManualFilter() && filter.getIsCheckWorkGroup() && webUser.isAnInsurer() && !webUser.getInsurer().isWorkgroupEnable()) {
                            LOG.debug("Not adding queue '{}' as workgroups not enabled.", filter.getName());
                            continue;
                        } else if (!filter.getIsManualFilter() && filter.getIsCheckOwnership() && webUser.isAnInsurer() && !webUser.getInsurer().isClaimOwnershipEnable()) {
                            LOG.debug("Not adding queue '{}' as claim ownership not enabled.", filter.getName());
                            continue;
                        } else if (filter.getIsCheckFnol() && webUser.isAnInsurer() && !webUser.getInsurer().isFnolEnable()) {
                            LOG.debug("Not adding queue '{}' as FNOL not enabled.", filter.getName());
                            continue;
                        } else if (filter.getIsCheckEngineers() && webUser.isAnInsurer() && !webUser.getInsurer().isEngineersEnable()) {
                            LOG.debug("Not adding queue '{}' as engineers not enabled.", filter.getName());
                            continue;
                        } else if (filter.getKey().equals(Filter.FILTER_INVOICE_UNASSIGNED) && webUser.isAnInsurer() && !webUser.getInsurer().isThirdPartyInterventionActivated()) {
                            LOG.debug("Not adding queue '{}' as TPI not enabled.", filter.getName());
                            continue;
                        } else if (filter.getIsManualFilter() && webUser.isAnInsurer() && !webUser.getInsurer().isUploadEnabled()) {
                            LOG.debug("Not adding queue '{}' as Insurer Upload not enabled.", filter.getName());
                            continue;
                        } else if (filter.getKey().equals(Filter.FILTER_MANUAL_INVOICES_TO_BE_ASSIGNED) && webUser.isAnInsurer()
                                && !webUser.getInsurer().isEnableManualInvoiceWorkgroups() && !webUser.getInsurer().isEnableManualInvoiceOwnership()) {
                                LOG.debug("Not adding queue '{}' as Manual Invoice workgroups/ownership not enabled.", filter.getName());
                                continue;
                        } else if (filter.getKey().equals(Filter.FILTER_REJECTED_SUBSCRIBER_CLAIMS)
                                && webUser.isCHO() && !webUser.getChorganisation().isEnableSubscriberClaims()) {
                            LOG.debug("Not adding queue '{}' as Subscriber claims not enabled.", filter.getName());
                            continue;
                        } else if (filter.getKey().equals(Filter.FILTER_ESCALATED_INVOICES_TO_SUPERVISOR) && webUser.isAnInsurer() && !webUser.getInsurer().isSupervisorEnable()) {
                                LOG.debug("Not adding queue '{}' as Supervisor is not enabled.", filter.getName());
                                continue;
                        }  else if (filter.getKey().equals(Filter.FILTER_REJECTED_FIXEDFEE_CLAIMS)
                                && webUser.isCHO() && !webUser.getChorganisation().isEnableFixedFeeClaims()) {
                            LOG.debug("Not adding queue '{}' as Subscriber claims not enabled.", filter.getName());
                            continue;
                        }
                        
                     
                        LOG.debug("Adding filter: '{}'", filter.getName());
                        filters.add(filter);
                    } else {
                        LOG.debug("Filter '{}' not accessible to user '{}'", filter.getKey(), webUser.getFullName());
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
