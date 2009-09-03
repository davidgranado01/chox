/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.data.SecurityInfoProvider;
import chox.model.WebUser;
import java.util.ArrayList;
import java.util.List;

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

                    // CREDIT HIRE USER
                    getCurrentSession().enableFilter("Claim_CHOFilter").setParameter("chorganisationId", this.getCurrentUser().getChorganisation().getId());
                    
                } else if (this.getSecurityInfoProvider().getIsINS()) {
                    
                    // INSURER USER
                    //  getCurrentSession().enableFilter("LineOfBusiness_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                    getCurrentSession().enableFilter("Claim_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                    getCurrentSession().enableFilter("Workgroup_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                    
                    if(isClaimHandlerOnly() && this.getSecurityInfoProvider().getCurrentUser().getInsurer().isWorkgroupEnable()){
                        
                        // getCurrentSession().enableFilter("Claim_WorkgroupFilter").setParameter("workgroupId", 48);

                    }
                }
            }
        }
    }
    
    public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUser();
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
        return this.securityInforProvider;
    }

}
