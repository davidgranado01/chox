package idas.chox.service.security;

import idas.chox.core.model.WebUser;

public class PanelAccessibility {

    private boolean fnolReviewedPanelAccessible;

    private String getPanelAccessibilityKey(String filterName) {
        return String.format("panel.%1$s", filterName);
    }


    public PanelAccessibility(ApplicationAccessibility accessibility, WebUser user) {
        fnolReviewedPanelAccessible = accessibility.checkAccessibilityForUser(getPanelAccessibilityKey(ApplicationAccessibility.PANEL_FNOL_REVIEWED), user) > 0;
    }

    public boolean getFnolReviewedPanelAccessible() {
        return fnolReviewedPanelAccessible;
    }

    public void setFnolReviewedPanelAccessible(boolean fnolReviewedPanelAccessible) {
        this.fnolReviewedPanelAccessible = fnolReviewedPanelAccessible;
    }
}
