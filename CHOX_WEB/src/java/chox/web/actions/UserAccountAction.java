/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.WebUser;
import chox.services.UserService;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.hibernate.util.StringHelper;

/**
 *
 * @author Emmanuel
 */
public class UserAccountAction extends BaseAction {
    
    private WebUser webUser;
    private String newPassword;
    private UserService userService;
    private String actionResult;
    private String message;
    
    @Override
    public String execute()
    {
        webUser = this.getAuthenticatedUser().getUser();
        if(webUser.getIsExpired())
        {
            message = "Your Password has expired. Please choose a new password.";
        }
        return SUCCESS;
    }

    public String changePassword()
    {
        webUser = this.getAuthenticatedUser().getUser();

        PasswordEncoder passwordEncoder = new org.acegisecurity.providers.encoding.Md5PasswordEncoder();
        webUser.setPassword(passwordEncoder.encodePassword(webUser.getPassword(), null));
        webUser.setIsExpired(false);
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

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    public boolean getIsShowMessage()
    {
        return StringHelper.isNotEmpty(message);
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    

}
