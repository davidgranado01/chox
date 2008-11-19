/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.web.security.PermissionedUser;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;

/**
 *
 * @author Emmanuel
 */
public class RouteAction extends BaseAction {

    public RouteAction() {
    }  
    
    @Override
    public String execute() throws Exception {
        
        //Internal route not pass thourh AcegiInterceptor, 
        //so we might not able t retrieve PermissionedUser from baseAction 
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        PermissionedUser user = (PermissionedUser)currentUser.getPrincipal();
        
        if(user != null)
        {
            if(user.getIsCHO())
            {
                return "cho";
            }        
            else if(user.getIsINS())
            {
                return "ins";
            }             
        }  
        
        return "error";
    }

}