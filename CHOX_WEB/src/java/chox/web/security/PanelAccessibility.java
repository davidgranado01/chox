/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.security;

import org.acegisecurity.GrantedAuthority;

/**
 *
 * @author Emmanuel
 */
public class PanelAccessibility {
    
    private boolean fnolReviewedPanelAccessible;
    
    public PanelAccessibility(GrantedAuthority[] grantedAuthorities) {
        ApplicationAccessibility accessibility = ApplicationAccessibility.getInstance();

        fnolReviewedPanelAccessible = accessibility.checkPanelAccessibility(ApplicationAccessibility.PANEL_FNOL_REVIEWED,
                grantedAuthorities) > 0;
    }

    public boolean getFnolReviewedPanelAccessible() {
        return fnolReviewedPanelAccessible;
    }

    public void setFnolReviewedPanelAccessible(boolean fnolReviewedPanelAccessible) {
        this.fnolReviewedPanelAccessible = fnolReviewedPanelAccessible;
    }
    
    

}
