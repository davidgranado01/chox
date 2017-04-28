package idas.chox.data.services;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.WebUser;

public class SecureDataService extends BaseDataService {

    private static final Logger LOG = LoggerFactory.getLogger(SecureDataService.class);
    private SecurityInfoProvider securityInfoProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider provider) {
        this.securityInfoProvider = provider;

        if (this.securityInfoProvider != null && this.securityInfoProvider.getCurrentUser() != null) {
            initGlobalFilter();
        }
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return this.securityInfoProvider;
    }

    public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUser();
    }

    private void initGlobalFilter() {
        if (!this.getSecurityInfoProvider().getIsCHOXAdmin()) {
            try {
                if (this.getSecurityInfoProvider().getIsCHO() && getCurrentSession().getEnabledFilter("cho_filter") == null) {
                    getCurrentSession().enableFilter("cho_filter").setParameterList("choIds", Arrays.asList(this.getCurrentUser().getChorganisation().getId()));
                    LOG.debug("CHO Filter set to {}", this.getCurrentUser().getChorganisation().getId());
                } else if (this.getSecurityInfoProvider().getIsINS() && getCurrentSession().getEnabledFilter("insurer_filter") == null) {
                    getCurrentSession().enableFilter("insurer_filter").setParameterList("insurerIds", Arrays.asList(this.getCurrentUser().getInsurer().getId()));
                    LOG.debug("Insurer filter set to {}", this.getCurrentUser().getInsurer().getId());
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown setting up db filters: {}", ex.getMessage(), ex);
            }
        }
    }
}
