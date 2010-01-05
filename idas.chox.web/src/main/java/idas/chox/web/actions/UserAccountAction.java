/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.WebUser;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DateHelper;
import org.hibernate.util.StringHelper;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.providers.encoding.PasswordEncoder;

public class UserAccountAction extends BaseAction {

    private WebUser webUser;
    private String newPassword;
    private UserService userService;
    private String actionResult;
    private String message;

    @Override
    public String execute() {
        webUser = this.getAuthenticatedUser();
        if (webUser.getIsExpired()) {
            message = "Your Password has expired. Please choose a new password.";
        }
        return SUCCESS;
    }

    public String changePassword() {
        try {
            webUser = this.getAuthenticatedUser();

            webUser.setLastModifiedDate(DateHelper.getCurrentDateTime());
            webUser.setLastModifiedBy(this.getAuthenticatedUser());

            PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
            webUser.setPassword(passwordEncoder.encodePassword(getNewPassword(), null));
            webUser.setIsExpired(false);
            userService.persist(webUser, webUser.getEmail());
            this.getActionResponse().AssignMessageResult("Your password has been changed.");
        } catch (Exception ex) {
            logger.error(ex);
            getActionResponse().AddError(ex.getMessage());
        }
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

    public String getMessage() {
        return message;
    }

    public boolean getIsShowMessage() {
        return StringHelper.isNotEmpty(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
