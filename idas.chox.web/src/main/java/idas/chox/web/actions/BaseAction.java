package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opensymphony.xwork2.ActionSupport;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.service.ActionResponse;
import net.sf.json.JSONObject;
import org.hibernate.StaleObjectStateException;
import org.springframework.security.AccessDeniedException;

public class BaseAction extends ActionSupport {

    private static final Logger LOG = LoggerFactory.getLogger(BaseAction.class);
    protected ActionResponse actionResponse;
    private String actionResult;
    private String actionError;
    private SecurityInfoProvider securityInfoProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    public WebUser getAuthenticatedUser() {
        return securityInfoProvider.getCurrentUser();
    }

    public boolean getIsCHO() {
        return securityInfoProvider.getIsCHO();
    }

    public boolean getIsAdmin() {
        return securityInfoProvider.getIsCHOXAdmin()
                || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)
                || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH_MNG);
    }
    
    public boolean getIsCH() {
        return securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH);
    }

    public boolean getIsOp() {
        return securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH_OPR);
    }

    public boolean getIsInsurer() {
        return securityInfoProvider.getIsINS();
    }

    public boolean getInsurerIsWorkgroupEnabled() {
        if (!getIsInsurer()) {
            LOG.debug("returning insurerIsWorkgroupEnabled: true (not insurer)");
            return true;
        }
        else {
            LOG.debug("returning insurerIsWorkgroupEnabled: {}", getAuthenticatedUser().getInsurer().isWorkgroupEnable());
            return getAuthenticatedUser().getInsurer().isWorkgroupEnable();
        }
    }

    public boolean getIsClaimOwnershipEnabled() {
        if (getIsCHO())
            return getAuthenticatedUser().getChorganisation().isClaimOwnershipEnable();
        else if (getIsInsurer())
            return getAuthenticatedUser().getInsurer().isClaimOwnershipEnable();

        return true;
    }

    public boolean getInsurerIsClaimOwnershipEnabled() {
        if (!getIsInsurer())
            return true;

        return getAuthenticatedUser().getInsurer().isClaimOwnershipEnable();
    }

    public boolean getChoIsClaimOwnershipEnabled() {
        if (!getIsCHO())
            return true;

        return getAuthenticatedUser().getChorganisation().isClaimOwnershipEnable();
    }

    public boolean isTaskManagementEnabled() {
        if (getIsInsurer()) {
            getAuthenticatedUser().getInsurer().isTaskManagementEnable();
        }
        else if (getIsCHO()) {
            return getAuthenticatedUser().getChorganisation().isTaskManagementEnable();
        }

        return true;
    }
    public boolean getInsurerIsFnolEnabled() {
        if (!getIsInsurer())
            return true;
        else
            return getAuthenticatedUser().getInsurer().isFnolEnable();
    }

    public boolean getInsurerIsEngineersEnabled() {
        if (!getIsInsurer())
            return true;
        else
            return getAuthenticatedUser().getInsurer().isEngineersEnable();
    }

    public boolean getInsurerOnlineSupportEnabled() {
        if (!getIsInsurer())
            return true;
        else
            return getAuthenticatedUser().getInsurer().isOnlineSupportEnable();
    }

    public boolean getIsChoxAdmin() {

        return securityInfoProvider.getIsCHOXAdmin();
    }

    public boolean getIsSupportEnabled() {
        if (getAuthenticatedUser().getInsurer() != null)
            return getAuthenticatedUser().getInsurer().isOnlineSupportEnable();

        return true;
    }

    public int getUserOrganisationType() {
        int iOrganisationType = 1;
        if (!securityInfoProvider.getIsCHOXAdmin()) {
            if (securityInfoProvider.getIsCHO()) {
                iOrganisationType = 3;
            } else if (securityInfoProvider.getIsINS()) {
                iOrganisationType = 2;
            }
        }
        return iOrganisationType;
    }

    public int getUserOrganisationId() {

        int iOrganisationId = 1;
        if (securityInfoProvider.getIsINS()) {
            iOrganisationId = getAuthenticatedUser().getInsurer().getId();
        } else if (securityInfoProvider.getIsCHO()) {
            iOrganisationId = getAuthenticatedUser().getChorganisation().getId();
        }

        return iOrganisationId;

    }

    public String getCurrentUserDesc() {
        WebUser user = getAuthenticatedUser();
        String logInUserDesc = user.getFirstName() + " " + user.getLastName();
        String strOrgType = "";

        if (securityInfoProvider.getIsCHO()) {
            strOrgType = user.getChorganisation().getName();
        } else if (securityInfoProvider.getIsINS()) {
            strOrgType = user.getInsurer().getName();
        }

        if (!strOrgType.equalsIgnoreCase("")) {
            logInUserDesc = logInUserDesc + ", " + strOrgType;
        }

        return logInUserDesc;
    }

    public String getSupportFile() {
        if (getIsInsurer())
            return getAuthenticatedUser().getInsurer().getSupportProcedure();
        else
            return "/chox_support.html";
    }
    
    public Integer getRoleTypeForHelpFile() {

        /*
        1: // NORMAL INSURER ROLE
        2: // INSURER MANAGER ROLE
        3: // NORMAL CREDIT HIRE ROLE
        4: // CREDIT HIRE MANAGER ROLE
         */

        Integer iRoleType = null;

        if (getIsInsurer()) {
            iRoleType = 1;
            if (securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)) {
                iRoleType = 2;
            }
        }

        if (getIsCHO()) {
            iRoleType = 3;
            if (securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH_MNG)) {
                iRoleType = 4;
            }
        }

        return iRoleType;
    }

    public ActionResponse getActionResponse() {
        if (actionResponse == null) {
            actionResponse = new ActionResponse();
        }
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    public String getActionResponseString() {
        JSONObject jsonObject = JSONObject.fromObject(getActionResponse());
        return jsonObject.toString();
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public String getActionError() {
        return actionError;
    }

    public void setActionError(String actionError) {
        this.actionError = actionError;
    }

    protected void handleException(Exception ex) {
        if (ex instanceof StaleObjectStateException) {
            LOG.warn("StaleObjectStateException thrown: {}", ex.getMessage());
        } else if (ex instanceof AccessDeniedException) {
            LOG.error("AccessDeniedException thrown: {}", ex.getMessage());
            throw new AccessDeniedException(ex.getMessage());
        } else {
            LOG.warn("handleException: exception is {}", ex.getMessage());
        }
        setActionError(formErrorMessage(ex));
        getActionResponse().AddError(actionError);
    }

    protected String formErrorMessage(Exception ex) {
        if (ex instanceof StaleObjectStateException) {
            return "Record was updated by another transaction/user, please try again.";
        }
        return ex.getLocalizedMessage();
    }
}
