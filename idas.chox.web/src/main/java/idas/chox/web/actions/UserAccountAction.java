package idas.chox.web.actions;

import idas.chox.core.model.WebUser;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import org.hibernate.util.StringHelper;

public class UserAccountAction extends BaseAction {
    
    private WebUser webUser;
    private String newPassword;
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

            ActionResponse response = adminUserService.updateUserPassword(this.getAuthenticatedUser().getId(), getNewPassword());
            setActionResponse(response);

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
