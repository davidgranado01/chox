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
                    
                    if(isClaimHandlerOnly()){
                        int lineOfBusinessId = -1;
                        if(getCurrentUser().getLineOfBusiness()!=null){
                            lineOfBusinessId = getCurrentUser().getLineOfBusiness().getId();
                        }
                        
                        getCurrentSession().enableFilter("LineOfBusiness_LineOfBusinessFilter").setParameter("lineOfBusinessId", lineOfBusinessId);
                        getCurrentSession().enableFilter("Claim_LineOfBusinessFilter").setParameter("lineOfBusinessId", lineOfBusinessId);
                    }
                }
            }
        }
    }
    
    public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUSer();
    }

    public boolean isClaimHandlerOnly(){
        
        boolean bFlag = false;
        
        WebUser user = getCurrentUser();
        if(user.isClaimHandler() && (user.getRoles().size()<=2)){
            bFlag = true;
        }
        return bFlag;
    }
    
    public SecurityInfoProvider getSecurityInfoProvider() {
        if (this.securityInforProvider == null) {
            setSecurityInfoProvider(new FakeSecurityInfoProvider());
        }
        return this.securityInforProvider;
    }

}
