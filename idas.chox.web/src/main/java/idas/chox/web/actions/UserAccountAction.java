package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.WebUser;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import org.hibernate.util.StringHelper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class UserAccountAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccountAction.class);
    private WebUser webUser;
    private String oldPassword;
    private String telephone;
    private String newPassword;
    private String confirmNewPassword;
    private String message;
    private AdminUserService adminUserService;
    private boolean redirect = false;
    private boolean showSplash;

    public boolean isShowSplash() {
        return showSplash;
    }

    public void setShowSplash(boolean showSplash) {
        this.showSplash = showSplash;
    }

    public int getMinPasswordLength() {
        if (webUser != null && webUser.isAnInsurer())
            return webUser.getInsurer().getMinimumPasswordLength();
        else if (webUser != null && !webUser.isCHOXAdmin())
            return webUser.getChorganisation().getMinimumPasswordLength();
        
        
        return 6;
    }

    public boolean getRedirect() {
        return redirect;
    }

    public void setRedirect(boolean redirect) {
        LOG.debug("Redirect set to {}", redirect);
        this.redirect = redirect;
    }

    @Override
    public String execute() {
        webUser = this.getAuthenticatedUser();
        if (webUser.getIsExpired()) {
            LOG.debug("Password for user {} has expired", webUser.getFullName());
            message = "Your Password has expired. Please choose a new password.";
        }
        return SUCCESS;
    }

    public String UserBrowserWarning() {
        try {
            LOG.info("Browser warning for user: '{}' user Id : '{}' will not be shown in future.", getAuthenticatedUser().getFullName(), getAuthenticatedUser().getId());
            ActionResponse response = adminUserService.updateUserBrowserWarning(getAuthenticatedUser().getId(),showSplash);
            setActionResponse(response);
            setActionResult((String) response.getResult());
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
            getActionResponse().AddError("Error: " + ex.getMessage());
            return ERROR;
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public String changePassword() {

        try {
            LOG.debug("Changing password from '{}' to '{}'...", getOldPassword(), getNewPassword());
            ActionResponse response = adminUserService.updateUserPassword(this.getAuthenticatedUser().getId(), getNewPassword(), getOldPassword());

            setActionResponse(response);
            if (response.getErrors().size() > 0) {
                setActionResult(response.getErrors().get(0));
                setActionError("Error changing password: " + response.getErrors().get(0));
            } else if (response.getResultType().equals(ActionResponse.RESULT_TYPE_MESSAGE)) {
                setActionResult((String) response.getResult());
                if (webUser == null) {
                    webUser = this.getAuthenticatedUser();
                }
                if (webUser.getIsExpired()) {
                    webUser.setIsExpired(Boolean.FALSE);
                    LOG.debug("WebUser password set to not expired.");
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
            getActionResponse().AddError("Error: " + ex.getMessage());
            setActionError("Error changing password: " + ex.getMessage());
            setActionResult("Error changing password: " + ex.getMessage());
        }

        return SUCCESS;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public String changeTelephone() {

        try {
            LOG.debug("Changing telephone from '{}' to '{}'", this.getAuthenticatedUser().getTelephone(), telephone);
            ActionResponse response = adminUserService.updateUserTelephone(this.getAuthenticatedUser().getId(), getTelephone());

            setActionResponse(response);
            if (response.getErrors().size() > 0) {
                LOG.debug("Error updating user contact telephone: {}", response.getErrors().get(0));
                setActionResult(response.getErrors().get(0));
                setActionError("Error changing password: " + response.getErrors().get(0));
            } else if (response.getResultType().equals(ActionResponse.RESULT_TYPE_MESSAGE)) {
                setActionResult((String) response.getResult());
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
