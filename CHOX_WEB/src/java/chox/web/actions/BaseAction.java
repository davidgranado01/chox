package chox.web.actions;

import chox.web.security.AcegiPrincipal;
import com.opensymphony.xwork2.ActionSupport;
import chox.web.security.PermissionedUser;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
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
        
        if(user == null)
        {
            Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
            if(currentUser != null)
            {
                user = (PermissionedUser)currentUser.getPrincipal();
            }
            
        }
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
