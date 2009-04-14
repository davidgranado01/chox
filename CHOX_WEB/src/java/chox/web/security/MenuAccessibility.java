/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.security;

import org.acegisecurity.GrantedAuthority;

/**
 *
 * @author Emmanuel
 */
public class MenuAccessibility {

    private boolean dashBoardMenuAccessibility;
     private boolean reportMenuAccessibility;
     private boolean adminMenuAccessibility;

    
    public MenuAccessibility(ApplicationAccessibility applicationAccessibility,GrantedAuthority[] grantedAuthorities) {       
               
        dashBoardMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_DASHBOARD,grantedAuthorities) > 0;
        reportMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_REPORT,grantedAuthorities) > 0;     
        adminMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_ADMIN,grantedAuthorities) > 0;     
    }

    public boolean getIsDashBoardMenuAccessibility() {
        return dashBoardMenuAccessibility;
    }

    public boolean getIsReportMenuAccessibility() {
        return reportMenuAccessibility;
    }

    public boolean getIsAdminMenuAccessibility() {
        return adminMenuAccessibility;
    }

}
