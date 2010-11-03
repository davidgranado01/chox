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
    private String telephone;
    private String newPassword;
    private String confirmNewPassword;
    private String message;
    private AdminUserService adminUserService;

    @Override
    public String execute() {
        webUser = this.getAuthenticatedUser();
        if (webUser.getIsExpired()) {
            LOG.debug("Password for user {} has expired", webUser.getFullName());
            message = "Your Password has expired. Please choose a new password.";
        }
        return SUCCESS;
    }

    public String changePassword() {

        try {
            LOG.debug("Changing password from '{}' to '{}'...", getOldPassword(), getNewPassword());
            ActionResponse response = adminUserService.updateUserPassword(this.getAuthenticatedUser().getId(), getNewPassword(), getOldPassword());

            setActionResponse(response);
            if (response.getErrors().size() > 0) {
                setActionResult(response.getErrors().get(0));
                setActionError("Error changing password: " + response.getErrors().get(0));
            }
            else if (response.getResultType().equals(response.RESULT_TYPE_MESSAGE))
                setActionResult((String)response.getResult());
        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
            getActionResponse().AddError("Error: " + ex.getMessage());
            setActionError("Error changing password: " + ex.getMessage());
            setActionResult("Error changing password: " + ex.getMessage());
        }
        if (webUser == null)
            webUser = this.getAuthenticatedUser();
        if (webUser.getIsExpired()) {
            webUser.setIsExpired(Boolean.FALSE);
            LOG.debug("WebUser password set to not expired.");
        }

        return SUCCESS;
    }

    public String changeTelephone() {

        try {
            LOG.debug("Changing telephone from '{}' to '{}'", this.getAuthenticatedUser().getTelephone(), telephone);
            ActionResponse response = adminUserService.updateUserTelephone(this.getAuthenticatedUser().getId(), getTelephone());

            setActionResponse(response);
            if (response.getErrors().size() > 0) {
                LOG.debug("Error updating user contact telephone: {}", response.getErrors().get(0));
                setActionResult(response.getErrors().get(0));
                setActionError("Error changing password: " + response.getErrors().get(0));
            }
            else if (response.getResultType().equals(response.RESULT_TYPE_MESSAGE)) {
                setActionResult((String)response.getResult());
                LOG.debug("Authenticated user contact number is '{}'.", getAuthenticatedUser().getTelephone());
                getAuthenticatedUser().setTelephone(telephone);
            }
       } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
            getActionResponse().AddError("Error: " + ex.getMessage());
            setActionError("Error changing contact telephone number: " + ex.getMessage());
            setActionResult("Error changing contact telephone number: " + ex.getMessage());
        }
        webUser = this.getAuthenticatedUser();
        return SUCCESS;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
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
