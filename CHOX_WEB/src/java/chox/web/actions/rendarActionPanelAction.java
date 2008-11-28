/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.web.security.ApplicationAccessibility;
import chox.web.security.PermissionedUser;
import org.acegisecurity.GrantedAuthority;
import chox.model.WebUserRole;
import java.util.List;
import chox.web.data.PanelAction;

public class rendarActionPanelAction extends BaseAction{
    
    //private int userRole;
    private String claimStatus;
    private PermissionedUser user;
    public static final String DECLINE = "decline";
    
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
        
        GrantedAuthority[] grantedAuthorities = getAuthenticatedUser().getAuthorities();
        List<String> actions = PanelAction.getPanelAction();
               
        for(String thisActionName : actions)
        {
            String sActionKey = "action."+thisActionName;
            String sClaimStatus = getClaimStatus();
            
            short accessRight = ApplicationAccessibility.getInstance().checkActionAccessibility(sActionKey, grantedAuthorities, sClaimStatus);
            
            if(accessRight>0){
                return thisActionName;
            }
        }

        return DECLINE;
    }
    
    
}
