/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.WebUser;
import chox.services.UserService;

/**
 *
 * @author Emmanuel
 */
public class UserAccountAction extends BaseAction {
    
    private WebUser webUser;
    private String newPassword;
    private UserService userService;
    private String actionResult;
    
    @Override
    public String execute()
    {
        webUser = this.getAuthenticatedUser().getUser();
        return SUCCESS;
    }

    public String changePassword()
    {
        webUser = this.getAuthenticatedUser().getUser();
        webUser.setPassword(newPassword);        
        userService.persist(webUser, webUser.getEmail());
        actionResult = "Your password has been changed.";
        return SUCCESS;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getActionResult() {
        return actionResult;
    }
    
    

}
