package chox.services;

import chox.Util.RoleHelper;
import chox.data.SecurityInfoProvider;
import chox.model.WebUser;
import chox.model.WebUserRole;
import java.util.Set;
import java.util.Iterator;
import java.util.Set;

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
                    
                    getCurrentSession().enableFilter("Claim_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                    getCurrentSession().enableFilter("Workgroup_InsurerFilter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                    
                    if(RoleHelper.isOwnWorkgroupRolesOnly(getCurrentUser()) && this.getSecurityInfoProvider().getCurrentUser().getInsurer().isWorkgroupEnable()){
                        getCurrentSession().enableFilter("Claim_WorkgroupFilter").setParameterList("workgroupIds", this.getCurrentUser().getWorkgroupIds());
                    }

                    if(RoleHelper.isClaimHandlerRoleOnly(getCurrentUser()) && this.getSecurityInfoProvider().getCurrentUser().getInsurer().isClaimOwnershipEnable()){
                        System.out.println(">> FILTER BY OWNERSHIP");
                        getCurrentSession().enableFilter("Claim_OwnershipFilter").setParameter("claimOwnershipId", this.getCurrentUser().getId());
                    }
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
