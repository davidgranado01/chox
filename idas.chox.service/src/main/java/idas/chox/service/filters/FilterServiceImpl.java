package idas.chox.service.filters;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.WebUser;
import idas.chox.core.model.Filter;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.FilterService;
import idas.chox.service.security.ApplicationAccessibility;

public class FilterServiceImpl implements FilterService, BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(FilterServiceImpl.class);
    private List<Filter> availableFilters;
    private BeanFactory beanFactory;
    private ApplicationAccessibility applicationAccessibility;
    private SecurityInfoProvider securityInfoProvider;

    @Override
    public List<Filter> getAvailableFilters(WebUser webUser) {

        List<Filter> filters = new ArrayList<>();

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
                        } else if (filter.getIsManualFilter() && webUser.isAnInsurer() && !webUser.getInsurer().isInvoiceUploadEnabled()) {
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
                        } else if (filter.getKey().equals(Filter.FILTER_ESCALATED_INVOICES_TO_SUPERVISOR) && webUser.isCHO()&& !webUser.getChorganisation().isSupervisorEnable()) {
                                LOG.debug("Not adding queue '{}' as Supervisor is not enabled.", filter.getName());
                                continue;
                        } else if (filter.getKey().equals(Filter.FILTER_REJECTED_FIXEDFEE_CLAIMS)
                                && webUser.isCHO() && !webUser.getChorganisation().isEnableFixedFeeClaims()) {
                            LOG.debug("Not adding queue '{}' as Subscriber claims not enabled.", filter.getName());
                            continue;
                        }  else if (filter.getKey().equals(Filter.FILTER_AWAITING_INVOICE_DATA)
                                && webUser.isAnInsurer() && !webUser.getInsurer().isClaimUploadEnabled()) {
                            LOG.debug("Not adding queue '{}' as claim upload not enabled.", filter.getName());
                            continue;
                        }  else if (filter.getKey().equals(Filter.FILTER_CLAIM_AWAITING_HIRE_MONITORING_INFO)
                                && webUser.isAnInsurer() && !webUser.getInsurer().isClaimUploadEnabled()) {
                            LOG.debug("Not adding queue '{}' as claim upload not enabled.", filter.getName());
                            continue;
                        } else if (filter.getKey().equals(Filter.FILTER_APPROVED_INVOICE_AWAITING_PAYMENT)
                                && webUser.isAnInsurer() && webUser.getInsurer().isPaymentsTeamEnable()
                                && securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_PC)
                                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_CH)
                                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MI)
                                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)) {
                            LOG.debug("Not adding queue '{}' as payment team is enabled and user is PC only", filter.getName());
                            continue;
                        } else if (filter.getKey().equals(Filter.FILTER_MANUAL_INVOICE_APPROVED)
                                && webUser.isAnInsurer() && webUser.getInsurer().isPaymentsTeamEnable()
                                && securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_PC)
                                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_CH)
                                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MI)
                                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_UPLOAD)
                                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)) {
                            LOG.debug("Not adding queue '{}' as payment team is enabled and user is PC only", filter.getName());
                            continue;
                        }  else if (filter.getKey().equals(Filter.FILTER_PAYMENT_TEAM)
                                && webUser.isAnInsurer() && !webUser.getInsurer().isPaymentsTeamEnable()) {
                            LOG.debug("Not adding queue '{}' as payment team is not enabled", filter.getName());
                            continue;
                        }  else if (filter.getKey().equals(Filter.FILTER_INVOICE_PAYMENT_DISPUTE)
                                && webUser.isAnInsurer() && (!webUser.getInsurer().isPaymentDisputesEnable()
                                            || (webUser.getInsurer().isPaymentsTeamEnable() && !webUser.isInRoleOf(WebUserRole.ROLE_INS_MNG) && !webUser.isInRoleOf(WebUserRole.ROLE_INS_CH)))) {
                            // Don't show the Payment Disputes queue if these are disabled or if payments team is active and we dont have CH or MNG role
                            // (as there will be the FILTER_PAYMENT_TEAM_DISPUTE queue for PC role)
                            LOG.debug("Not adding queue '{}' as payment disputes are not enabled", filter.getName());
                            continue;
                        }   else if (filter.getKey().equals(Filter.FILTER_PAYMENT_TEAM_DISPUTE)
                                && webUser.isAnInsurer() && (!webUser.getInsurer().isPaymentDisputesEnable()
                                || !webUser.getInsurer().isPaymentsTeamEnable())) {
                            LOG.debug("Not adding queue '{}' as payment disputes or team are not enabled", filter.getName());
                            continue;
                        } else if (filter.getKey().equals(Filter.FILTER_CASE_WITH_SOLICITOR)
                                && webUser.isCHO() && !webUser.getChorganisation().isSolicitorEnable()) {
                            LOG.debug("Not adding queue '{}' as queue not enabled.", filter.getName());
                            continue;
                        } else if (filter.getKey().equals(Filter.FILTER_CLAIMS_REQUIRING_AUDIT)
                                && webUser.isAnInsurer() && !webUser.getInsurer().isClaimAuditReviewEnable()) {
                            LOG.debug("Not adding queue '{}' as queue not enabled.", filter.getName());
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

    public SecurityInfoProvider getSecurityInfoProvider() {
        return securityInfoProvider;
    }

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }
}
