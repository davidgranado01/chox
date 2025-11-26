package idas.chox.service.security;

import idas.chox.core.model.WebUser;

public class AdminAccessibility {
    private static final String ADMIN_INSURER_COMPANIES = "InsurerCompanies";
    private static final String ADMIN_CREDIT_HIRE_ORG = "CreditHireOrg";
    private static final String ADMIN_USER_MANAGEMENT = "UserManagement";
    private static final String ADMIN_INSURER_BRE_MANAGEMENT = "InsurerBreManagement";
    private static final String ADMIN_BILLING = "Billing";

    private boolean insurerCompaniesAdminAccessibility;
    private boolean creditHireOrgAdminAccessibility;
    private boolean userManagementAdminAccessibility;
    private boolean isInsurerBreManagementAdminAccessibility;
    private boolean billingAdminAccessibility;
    private boolean supplierRateAccessibility;
    private boolean isGTARateAccessibility;

    public AdminAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user) {
        insurerCompaniesAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ADMIN_INSURER_COMPANIES, user) > 0;
        creditHireOrgAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ADMIN_CREDIT_HIRE_ORG, user) > 0;
        userManagementAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ADMIN_USER_MANAGEMENT, user) > 0;
        isInsurerBreManagementAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ADMIN_INSURER_BRE_MANAGEMENT, user) > 0;
        billingAdminAccessibility = applicationAccessibility.checkAdminAccessibility(ADMIN_BILLING, user) > 0;
        supplierRateAccessibility = user.isInRoleOf("ROLE_CHOX_ADMIN");
        isGTARateAccessibility = user.isInRoleOf("ROLE_CHOX_ADMIN");
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

    public boolean getIsSupplierRateAccessibility() {
        return supplierRateAccessibility;
    }

    public boolean isGTARateAccessibility() {
        return isGTARateAccessibility;
    }

}
