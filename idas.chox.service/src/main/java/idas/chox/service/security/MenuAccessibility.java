package idas.chox.service.security;

import java.util.Set;

public class MenuAccessibility {

    private boolean dashBoardMenuAccessibility;
    private boolean reportMenuAccessibility;
    private boolean adminMenuAccessibility;

    public MenuAccessibility(ApplicationAccessibility applicationAccessibility, Set roles) {

        dashBoardMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_DASHBOARD, roles) > 0;
        reportMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_REPORT, roles) > 0;
        adminMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_ADMIN, roles) > 0;
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
