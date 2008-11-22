package chox.web.actions;

import chox.web.security.AcegiPrincipal;
import com.opensymphony.xwork2.ActionSupport;
import chox.web.security.PermissionedUser;
/**
 * @author Emmanuel Kong
 * @version 
 */
public class BaseAction extends ActionSupport {

    private PermissionedUser user;

    @AcegiPrincipal
    public void setAuthenticatedUser(PermissionedUser user) {
        this.user = user;
    }

    public PermissionedUser getAuthenticatedUser() {
        return user;
    }
    
    public boolean getIsCHO()
    {
        return user.getIsCHO();
    }
    
    public boolean getIsInsurer()
    {
        return user.getIsINS();
    }
}
