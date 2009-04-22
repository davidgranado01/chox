package chox.web.actions;

import chox.model.Chorganisation;
import chox.model.WebUserRole;
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
        return getAuthenticatedUser().getIsCHO();
    }
    
    public boolean getIsInsurer()
    {
        return getAuthenticatedUser().getIsINS();
    }
    
    public boolean getIsChoxAdmin(){
        
        boolean isChoxAdmin = false;
        
        if(getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_CHOX)){
            isChoxAdmin = true;
        }
        
        return isChoxAdmin;
    }
    
    public String getCurrentUserDesc(){
        String logInUserDesc = user.getUser().getFirstName() + " " + user.getUser().getLastName();
        String strOrgType = "";
        
        if(user.getIsCHO()){
            strOrgType = user.getUser().getChorganisation().getName();
        }else if(user.getIsINS()){
            strOrgType = user.getUser().getInsurer().getName();
        }
        
        if(!strOrgType.equalsIgnoreCase("")){
            logInUserDesc = logInUserDesc + ", " + strOrgType;
        }
        
        return logInUserDesc;
    }
}
