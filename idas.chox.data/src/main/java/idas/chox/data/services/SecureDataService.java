package idas.chox.data.services;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.WebUser;

public class SecureDataService extends BaseDataService {

    private SecurityInfoProvider securityInforProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider provider) {

        this.securityInforProvider = provider;

        if (this.securityInforProvider != null && this.securityInforProvider.getCurrentUser() != null) {

            initGlobalFilter();
        }
    }

    public void initGlobalFilter() {
        if (!this.getSecurityInfoProvider().getIsCHOXAdmin()) {

            if (this.getSecurityInfoProvider().getIsCHO()) {
                if (getCurrentSession().getEnabledFilter("Claim_CHOFilter") == null) {
                    getCurrentSession().enableFilter("Claim_CHOFilter").setParameter("chorganisationId", this.getCurrentUser().getChorganisation().getId());
                }

            } else if (this.getSecurityInfoProvider().getIsINS()) {

                if (getCurrentSession().getEnabledFilter("Claim_InsurerFilter") == null) {
                    getCurrentSession().enableFilter("Claim_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                }
                if (getCurrentSession().getEnabledFilter("Workgroup_InsurerFilter") == null) {
                    getCurrentSession().enableFilter("Workgroup_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                }
            }
        }
    }

    public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUser();
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return this.securityInforProvider;
    }
}
