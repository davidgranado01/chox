package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.WebUser;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import org.hibernate.util.StringHelper;

public class UserAccountAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(UserAccountAction.class);
    
    private WebUser webUser;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String message;
    private AdminUserService adminUserService;

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
            LOG.debug("Changing password...");
            ActionResponse response = adminUserService.updateUserPassword(this.getAuthenticatedUser().getId(), getNewPassword(), getOldPassword());
            setActionResponse(response);

        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
            getActionResponse().AddError("Error: " + ex.getMessage());
        }
        return SUCCESS;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

    public String getMessage() {
        return message;
    }

    public boolean getIsShowMessage() {
        return StringHelper.isNotEmpty(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }
}
