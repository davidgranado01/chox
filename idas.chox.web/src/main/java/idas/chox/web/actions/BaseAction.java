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

    public boolean getIsInsurer() {
        return securityInfoProvider.getIsINS();
    }

    public boolean getIsChoxAdmin() {

        return securityInfoProvider.getIsCHOXAdmin();
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
        LOG.error("Exception thrown: {}", ex.getMessage());
        LOG.error("Caused by: {}", ex.getCause().getMessage());
        setActionError(formErrorMessage(ex));
        getActionResponse().AddError(actionError);

//        ex.printStackTrace();
    }

    protected String formErrorMessage(Exception ex) {
        if (ex instanceof StaleObjectStateException) {
            return "Record was updated by another transaction/user, please try again.";
        }
        return ex.getLocalizedMessage();
    }
}
