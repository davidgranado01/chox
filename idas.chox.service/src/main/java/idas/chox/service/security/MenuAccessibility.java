package idas.chox.service.security;

import idas.chox.core.model.WebUser;

public class MenuAccessibility {

    private boolean dashBoardMenuAccessibility;
    private boolean reportMenuAccessibility;
    private boolean adminMenuAccessibility;
    private boolean inboxMenuAccessibility;
    private boolean searchMenuAccessibility;
    private boolean uploadMenuAccessibility;

    public MenuAccessibility(ApplicationAccessibility applicationAccessibility, WebUser user) {

        dashBoardMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_DASHBOARD, user) > 0;
        reportMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_REPORT, user) > 0;
        adminMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_ADMIN, user) > 0;
        inboxMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_INBOX, user) > 0;
        searchMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_SEARCH, user) > 0;
        uploadMenuAccessibility = applicationAccessibility.checkMenuAccessibility(ApplicationAccessibility.MENU_UPLOAD, user) > 0;
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
    
    public boolean getIsInboxMenuAccessibility() {
        return inboxMenuAccessibility;
    }
    
    public boolean getIsSearchMenuAccessibility() {
        return searchMenuAccessibility;
    }

    public boolean getIsUploadMenuAccessibility() {
        return uploadMenuAccessibility;
    }
    
}
