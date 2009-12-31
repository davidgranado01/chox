/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.security;

import java.util.Set;

public class AdminAccessibility {

    private boolean insurerCompaniesAdminAccessibility;
    private boolean creditHireOrgAdminAccessibility;
    private boolean userManagementAdminAccessibility;
    private boolean isInsurerBreManagementAdminAccessibility;

    public AdminAccessibility(ApplicationAccessibility applicationAccessibility, Set roles) {
        insurerCompaniesAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ApplicationAccessibility.ADMIN_INSURER_COMPANIES, roles) > 0;
        creditHireOrgAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ApplicationAccessibility.ADMIN_CREDIT_HIRE_ORG, roles) > 0;
        userManagementAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ApplicationAccessibility.ADMIN_USER_MANAGEMENT, roles) > 0;
        isInsurerBreManagementAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ApplicationAccessibility.ADMIN_INSURER_BRE_MANAGEMENT, roles) > 0;
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

    public boolean isIsInsurerBreManagementAdminAccessibility() {
        return isInsurerBreManagementAdminAccessibility;
    }
}
