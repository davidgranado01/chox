package chox.web.security;

import org.acegisecurity.GrantedAuthority;

public class PanelAccessibility {
    
    private boolean fnolReviewedPanelAccessible;
    
    public PanelAccessibility(ApplicationAccessibility accessibility,GrantedAuthority[] grantedAuthorities) {
        fnolReviewedPanelAccessible = accessibility.checkPanelAccessibility(ApplicationAccessibility.PANEL_FNOL_REVIEWED, grantedAuthorities) > 0;
    }

    public boolean getFnolReviewedPanelAccessible() {
        return fnolReviewedPanelAccessible;
    }

    public void setFnolReviewedPanelAccessible(boolean fnolReviewedPanelAccessible) {
        this.fnolReviewedPanelAccessible = fnolReviewedPanelAccessible;
    }
    
    

}
