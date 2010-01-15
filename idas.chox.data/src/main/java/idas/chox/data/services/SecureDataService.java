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
                if (getCurrentSession().getEnabledFilter("History_IsPublicFilter") == null) {
                    getCurrentSession().enableFilter("History_IsPublicFilter").setParameter("isPublic", true);
                }
                if (getCurrentSession().getEnabledFilter("Comment_VisibilityTypesFilter") == null) {
                    getCurrentSession().enableFilter("Comment_VisibilityTypesFilter").setParameter("visibilityType", 2);
                }

            } else if (this.getSecurityInfoProvider().getIsINS()) {

                if (getCurrentSession().getEnabledFilter("Claim_InsurerFilter") == null) {
                    getCurrentSession().enableFilter("Claim_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                }
                if (getCurrentSession().getEnabledFilter("Workgroup_InsurerFilter") == null) {
                    getCurrentSession().enableFilter("Workgroup_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                }
                if (getCurrentSession().getEnabledFilter("Comment_VisibilityTypesFilter") == null) {
                    getCurrentSession().enableFilter("Comment_VisibilityTypesFilter").setParameter("visibilityType", 1);
                }
            }
        }
        System.out.println("Global filter initial");
    }

    public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUser();
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return this.securityInforProvider;
    }
}
