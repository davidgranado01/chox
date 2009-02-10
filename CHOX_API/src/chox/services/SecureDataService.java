/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.data.FakeSecurityInfoProvider;
import chox.data.SecurityInfoProvider;
import chox.model.WebUser;

/**
 *
 * @author Emmanuel
 */
public class SecureDataService extends DataService {
    
     private SecurityInfoProvider securityInforProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider provider) {
        this.securityInforProvider = provider;
        
        if (this.securityInforProvider != null) {

            if (!this.getSecurityInfoProvider().getIsCHOXAdmin()) {
                if (this.getSecurityInfoProvider().getIsCHO()) {
                    getCurrentSession().enableFilter("Claim_CHOFilter").setParameter("chorganisationId", this.getCurrentUser().getChorganisation().getId());
                } else if (this.getSecurityInfoProvider().getIsINS()) {
                    getCurrentSession().enableFilter("Claim_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                    getCurrentSession().enableFilter("LineOfBusiness_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                }
            }
        }
    }
    
     public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUSer();
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        if (this.securityInforProvider == null) {
            //for testing purpose, will inject by spring in web application
            setSecurityInfoProvider(new FakeSecurityInfoProvider());
        }
        return this.securityInforProvider;
    }

}
