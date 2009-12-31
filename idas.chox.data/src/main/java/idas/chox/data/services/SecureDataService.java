package idas.chox.data.services;

import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.WebUser;

public class SecureDataService extends BaseDataService {

    private SecurityInfoProvider securityInforProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider provider) {

        this.securityInforProvider = provider;

        if (this.securityInforProvider != null && this.securityInforProvider.getCurrentUser() != null) {

            if (!this.getSecurityInfoProvider().getIsCHOXAdmin()) {

                if (this.getSecurityInfoProvider().getIsCHO()) {
                    getCurrentSession().enableFilter("Claim_CHOFilter").setParameter("chorganisationId", this.getCurrentUser().getChorganisation().getId());
                    getCurrentSession().enableFilter("History_IsPublicFilter").setParameter("isPublic", true);
                    getCurrentSession().enableFilter("Comment_VisibilityTypesFilter").setParameter("visibilityType", 2);

                } else if (this.getSecurityInfoProvider().getIsINS()) {

                    getCurrentSession().enableFilter("Claim_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                    getCurrentSession().enableFilter("Workgroup_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                    getCurrentSession().enableFilter("Comment_VisibilityTypesFilter").setParameter("visibilityType", 1);
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
