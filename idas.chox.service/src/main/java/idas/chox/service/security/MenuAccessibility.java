package idas.chox.service.security;

import idas.chox.core.model.WebUser;

public class MenuAccessibility {

    private boolean dashBoardMenuAccessibility;
    private boolean reportMenuAccessibility;
    private boolean adminMenuAccessibility;

    public MenuAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user) {

        dashBoardMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_DASHBOARD, user) > 0;
        reportMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_REPORT, user) > 0;
        adminMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_ADMIN, user) > 0;
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
