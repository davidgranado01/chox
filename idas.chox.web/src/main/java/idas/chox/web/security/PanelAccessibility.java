package idas.chox.web.security;

import java.util.Set;

public class PanelAccessibility {

    private boolean fnolReviewedPanelAccessible;

    public PanelAccessibility(ApplicationAccessibility accessibility, Set roles) {
        fnolReviewedPanelAccessible = accessibility.checkPanelAccessibility(ApplicationAccessibility.PANEL_FNOL_REVIEWED, roles) > 0;
    }

    public boolean getFnolReviewedPanelAccessible() {
        return fnolReviewedPanelAccessible;
    }

    public void setFnolReviewedPanelAccessible(boolean fnolReviewedPanelAccessible) {
        this.fnolReviewedPanelAccessible = fnolReviewedPanelAccessible;
    }
}
