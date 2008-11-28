/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.web.security.PermissionedUser;
import org.acegisecurity.GrantedAuthority;
import chox.model.WebUserRole;

public class rendarActionPanelAction extends BaseAction{
    
    //private int userRole;
    private String claimStatus;
    private PermissionedUser user;

    
    
    
    public PermissionedUser getUser() {
        return user;
    }

    public void setUser(PermissionedUser user) {
        this.user = user;
    }
    
    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }
    
    /*
    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }
    */
    
    @Override
    public String execute() throws Exception {
        
        // GrantedAuthority[] grantedAuthorities = getAuthenticatedUser().getAuthorities();
        // grantedAuthorities
        
       PermissionedUser user = super.getAuthenticatedUser();
                
        //user.getAuthorities()
        
        return "";
    }
    
    
}
