package idas.chox.service.security;

import idas.chox.core.model.WebUser;

public class PanelAccessibility {

    private boolean fnolReviewedPanelAccessible;

    public PanelAccessibility(ApplicationAccessibility accessibility, WebUser user) {
        fnolReviewedPanelAccessible = accessibility.checkPanelAccessibility(ApplicationAccessibility.PANEL_FNOL_REVIEWED, user) > 0;
    }

    public boolean getFnolReviewedPanelAccessible() {
        return fnolReviewedPanelAccessible;
    }

    public void setFnolReviewedPanelAccessible(boolean fnolReviewedPanelAccessible) {
        this.fnolReviewedPanelAccessible = fnolReviewedPanelAccessible;
    }
}
