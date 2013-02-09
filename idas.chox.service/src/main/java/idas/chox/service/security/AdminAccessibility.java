package idas.chox.service.security;

import idas.chox.core.model.WebUser;

public class AdminAccessibility {

    private boolean insurerCompaniesAdminAccessibility;
    private boolean creditHireOrgAdminAccessibility;
    private boolean userManagementAdminAccessibility;
    private boolean isInsurerBreManagementAdminAccessibility;
    private boolean billingAdminAccessibility;

    private String getAdminAccessibilityKey(String adminName) {
        return String.format("admin.%1$s", adminName);
    }

    public AdminAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user) {
        insurerCompaniesAdminAccessibility = applicationAccessibility.checkAccessibilityForUser(getAdminAccessibilityKey(ApplicationAccessibility.ADMIN_INSURER_COMPANIES), user) > 0;
        creditHireOrgAdminAccessibility = applicationAccessibility.checkAccessibilityForUser(getAdminAccessibilityKey(ApplicationAccessibility.ADMIN_CREDIT_HIRE_ORG), user) > 0;
        userManagementAdminAccessibility = applicationAccessibility.checkAccessibilityForUser(getAdminAccessibilityKey(ApplicationAccessibility.ADMIN_USER_MANAGEMENT), user) > 0;
        isInsurerBreManagementAdminAccessibility = applicationAccessibility.checkAccessibilityForUser(getAdminAccessibilityKey(ApplicationAccessibility.ADMIN_INSURER_BRE_MANAGEMENT), user) > 0;
        billingAdminAccessibility = applicationAccessibility.checkAccessibilityForUser(getAdminAccessibilityKey(ApplicationAccessibility.ADMIN_BILLING), user) > 0;
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

    public boolean getIsBillingAdminAccessibility() {
        return billingAdminAccessibility;
    }
}
