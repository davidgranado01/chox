package idas.chox.web.actions;

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

    public String loadAdminPanel() {
        return this.adminPanelName;
    }

    public String getAdminPanelName() {
        return adminPanelName;
    }

    public void setAdminPanelName(String adminPanelName) {
        this.adminPanelName = adminPanelName;
    }

    public String getActionResult() {
        return actionResult;
    }

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