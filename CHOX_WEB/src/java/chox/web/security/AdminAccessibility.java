/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.security;

import org.acegisecurity.GrantedAuthority;

public class AdminAccessibility {

    private boolean insurerCompaniesAdminAccessibility;
    private boolean creditHireOrgAdminAccessibility;
    private boolean userManagementAdminAccessibility;

    public AdminAccessibility(ApplicationAccessibility applicationAccessibility,GrantedAuthority[] grantedAuthorities) {       
        insurerCompaniesAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ApplicationAccessibility.ADMIN_INSURER_COMPANIES,grantedAuthorities) > 0;
        creditHireOrgAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ApplicationAccessibility.ADMIN_CREDIT_HIRE_ORG,grantedAuthorities) > 0;     
        userManagementAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ApplicationAccessibility.ADMIN_USER_MANAGEMENT,grantedAuthorities) > 0;     
    }

    public boolean getIsCreditHireOrgAdminAccessibility() {
        return creditHireOrgAdminAccessibility;
    }

    public boolean getIsInsurerCompaniesAdminAccessibility() {
        return insurerCompaniesAdminAccessibility;
    }

    public boolean getIsUserManagementAdminAccessibility() {
        return userManagementAdminAccessibility;
    }



}
