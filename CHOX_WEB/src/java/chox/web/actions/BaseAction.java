package chox.web.actions;

import chox.model.Chorganisation;
import chox.model.WebUserRole;
import chox.web.security.AcegiPrincipal;
import com.opensymphony.xwork2.ActionSupport;
import chox.web.security.PermissionedUser;
import chox.web.viewdata.ActionResponse;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;

/**
 * @author Emmanuel Kong
 * @version 
 */

public class BaseAction extends ActionSupport {

    private PermissionedUser user;
    private ActionResponse actionResponse;
   
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
    
    public Integer getRoleTypeForHelpFile(){

        /*
        1: // NORMAL INSURER ROLE
        2: // INSURER MANAGER ROLE
        3: // NORMAL CREDIT HIRE ROLE
        4: // CREDIT HIRE MANAGER ROLE
        */

        Integer iRoleType = null;

        if(getIsInsurer()){
            iRoleType = 1;
            if(getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_INS_MNG)){
                iRoleType = 2;
            }
        }
        
        if(getIsCHO()){
            iRoleType = 3;
            if(getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_CH_MNG)){
                iRoleType = 4;
            }
        }

        return iRoleType;
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

    /**
     * @return the actionResponse
     */
    public ActionResponse getActionResponse() {
        if(actionResponse == null)
        {
            actionResponse = new ActionResponse();
        }
        return actionResponse;
    }
    
}
