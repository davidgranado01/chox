package idas.chox.web.actions;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.WebUser;
import idas.chox.service.security.AdminAccessibility;
import idas.chox.service.security.ApplicationAccessibility;

public class AdminAction extends BaseAction {

    private String adminPanelName;
    private String actionResult;
    private Integer tabIndex;
    private AdminAccessibility adminAccessibility;
    private ApplicationAccessibility applicationAccessibility;

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    public AdminAccessibility getAdminAccessibility() {

        if (adminAccessibility == null) {
            adminAccessibility = applicationAccessibility.getAdminAccessibility(super.getAuthenticatedUser());
        }
        return adminAccessibility;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String adminPanel() {
        return SUCCESS;
    }


    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER", "ROLE_INS_ADMIN"})
    public String loadAdminPanel() {
        // Check User has access to Admin Panel name requested
        if (("ChoxInsurerMgmtPanel".equals(adminPanelName) && !getAdminAccessibility().getIsInsurerCompaniesAdminAccessibility())
            || ("ChoxCreditHireMgmtPanel".equals(adminPanelName) && !getAdminAccessibility().getIsCreditHireOrgAdminAccessibility())
            || ("UserMgmt".equals(adminPanelName) && !getAdminAccessibility().getIsUserManagementAdminAccessibility())
            || ("UserroleMapping".equals(adminPanelName) && !getAdminAccessibility().getIsUserManagementAdminAccessibility())
            || ("InsurerPanelMgmt".equals(adminPanelName) && !getIsChoxAdmin() && (!getIsInsurer() || !getIsAdmin()))) {
            throw new AccessDeniedException("You do not have the privileges to access the requested resource. You will now be logged out.");
        } 
                    
        return this.adminPanelName;
    }

    public String getAdminPanelName() {
        return adminPanelName;
    }

    public void setAdminPanelName(String adminPanelName) {
        this.adminPanelName = adminPanelName;
    }

    @Override
    public String getActionResult() {
        return actionResult;
    }

    @Override
    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public WebUser getCurrentUser() {
        return getAuthenticatedUser();
    }

    public int getCurrentUserOrganisationType() {
        return getUserOrganisationType();
    }

    public int getCurrentUserOrganisationId() {
        return getUserOrganisationId();
    }
}