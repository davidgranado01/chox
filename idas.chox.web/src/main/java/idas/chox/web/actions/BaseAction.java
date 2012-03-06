package idas.chox.web.actions;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.service.ActionResponse;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements SessionAware {

    private static final Logger LOG = LoggerFactory.getLogger(BaseAction.class);
    protected ActionResponse actionResponse;
    private String actionResult;
    private String actionError;
    private SecurityInfoProvider securityInfoProvider;
    private Map<String,Object> session;
    private String VALID_SESSION = "validSession";

    public Map<String,Object> getSession() {
    	if(session == null)
    		session = new HashMap<String, Object>();//TODO session is sometimes null ?!?
        return session;
    }

    @Override
    public void setSession(Map<String,Object> session) {
        this.session = session;
    }

    public boolean isSearchHistory() {
        if (session.containsKey("searchHistory")) {
            return true;
        } else {
            return false;
        }
    }

    public void setSearchHistory(boolean searchHistory) {
        if (session !=null && !session.containsKey("searchHistory") && searchHistory) {
            session.put("searchHistory", searchHistory);
        }
    }

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    public WebUser getAuthenticatedUser() {
        return securityInfoProvider.getCurrentUser();
    }

    public boolean getIsCHO() {
        return securityInfoProvider.getIsCHO();
    }

    public boolean getIsUploadAllowed() {
        boolean allowed = false;
        
        if (getIsInsurer()) {
            allowed = getAuthenticatedUser().getInsurer().isUploadEnabled() && securityInfoProvider.isInRoleOf(WebUserRole.ROLE_UPLOAD);
        }
        return securityInfoProvider.getIsCHO() || allowed;
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

    public int getInvoiceLiabilityDisputeReasonId() {
        return 11;
    }

    public boolean getInsurerIsWorkgroupEnabled() {
        if (!getIsInsurer()) {
            LOG.debug("returning insurerIsWorkgroupEnabled: true (not insurer)");
            return true;
        } else {
            LOG.debug("returning insurerIsWorkgroupEnabled: {}", getAuthenticatedUser().getInsurer().isWorkgroupEnable());
            return getAuthenticatedUser().getInsurer().isWorkgroupEnable();
        }
    }

    public boolean getInsurerIsUploadEnabled() {
        if (!getIsInsurer()) {
            LOG.debug("returning insurerIsUploadEnabled: true (not insurer)");
            return true;
        } else {
            LOG.debug("returning insurerIsUploadEnabled: {}", getAuthenticatedUser().getInsurer().isUploadEnabled());
            return getAuthenticatedUser().getInsurer().isUploadEnabled();
        }
    }

    public boolean getIsTpiEnabledEnabled() {
        if (getIsCHO()) {
            LOG.debug("returning isTpiEnabled: {}", getAuthenticatedUser().getChorganisation().isThirdPartyInterventionActivated());
            return getAuthenticatedUser().getChorganisation().isThirdPartyInterventionActivated();
        } else if (getIsInsurer()) {
            LOG.debug("returning isTpiEnabled: {}", getAuthenticatedUser().getInsurer().isThirdPartyInterventionActivated());
            return getAuthenticatedUser().getInsurer().isThirdPartyInterventionActivated();
        } else {
            return true;
        }
    }

    public boolean getIsClaimOwnershipEnabled() {
        if (getIsCHO()) {
            return getAuthenticatedUser().getChorganisation().isClaimOwnershipEnable();
        } else if (getIsInsurer()) {
            return getAuthenticatedUser().getInsurer().isClaimOwnershipEnable();
        }

        return true;
    }

    public boolean getIsSubscriberEnabled() {
        if (getIsCHO()) {
            return getAuthenticatedUser().getChorganisation().isEnableSubscriberClaims();
        }
        else if (getIsInsurer()) {
            return getAuthenticatedUser().getInsurer().isAllowSubscriberClaims();
        }
        
        return true;
    }
    public boolean getInsurerIsClaimOwnershipEnabled() {
        if (!getIsInsurer()) {
            return true;
        }

        return getAuthenticatedUser().getInsurer().isClaimOwnershipEnable();
    }

    public boolean getChoIsClaimOwnershipEnabled() {
        if (!getIsCHO()) {
            return true;
        }

        return getAuthenticatedUser().getChorganisation().isClaimOwnershipEnable();
    }

    public boolean isTaskManagementEnabled() {
        if (getIsInsurer()) {
            return getAuthenticatedUser().getInsurer().isTaskManagementEnable();
        } else if (getIsCHO()) {
            return getAuthenticatedUser().getChorganisation().isTaskManagementEnable();
        }

        return true;
    }

    public boolean getInsurerIsFnolEnabled() {
        if (!getIsInsurer()) {
            return true;
        } else {
            return getAuthenticatedUser().getInsurer().isFnolEnable();
        }
    }

    public boolean getInsurerIsEngineersEnabled() {
        if (!getIsInsurer()) {
            return true;
        } else {
            return getAuthenticatedUser().getInsurer().isEngineersEnable();
        }
    }

    public boolean getInsurerOnlineSupportEnabled() {
        if (!getIsInsurer()) {
            return true;
        } else {
            return getAuthenticatedUser().getInsurer().isOnlineSupportEnable();
        }
    }

    public boolean getIsChoxAdmin() {

        return securityInfoProvider.getIsCHOXAdmin();
    }

    public boolean getIsSupportEnabled() {
        if (getAuthenticatedUser().getInsurer() != null) {
            return getAuthenticatedUser().getInsurer().isOnlineSupportEnable();
        }

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
        if (getIsInsurer()) {
            return getAuthenticatedUser().getInsurer().getSupportProcedure();
        } else {
            return "/chox_support.html";
        }
    }

    public Integer getBespokeHelpFileType() {
        Integer helpFileType = 0;
        /*
         * 0: normal help file
         * 1: help file for no FNOL or Engineers, Workgroups or Claim Handlers
         */
        if (getAuthenticatedUser().getInsurer() != null) {
            Insurer insurer = getAuthenticatedUser().getInsurer();
            if (!insurer.isEngineersEnable() && !insurer.isWorkgroupEnable() && !insurer.isClaimOwnershipEnable()) {
                helpFileType = 1;
            }
        }

        return helpFileType;
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
        if (actionError.length() == 0) {
            actionError = null;
            LOG.warn("Empty error string set for ActionError - setting to null.");
        }
        this.actionError = actionError;
    }

    protected void handleException(Exception ex) {
        if (ex instanceof StaleObjectStateException || ex instanceof HibernateOptimisticLockingFailureException) {
            LOG.warn("StaleObjectStateException thrown: {}", ex.getMessage());
        } else if (ex instanceof AccessDeniedException) {
            LOG.error("AccessDeniedException thrown: {}", ex.getMessage());
            throw new AccessDeniedException(ex.getMessage());
        } else {
            LOG.warn("handleException: exception is {} of class '{}'", ex.getMessage(), ex.getClass());
        }
        setActionError(formErrorMessage(ex));
        getActionResponse().AddError(actionError);
    }

    protected String formErrorMessage(Exception ex) {
        if (ex instanceof StaleObjectStateException || ex instanceof HibernateOptimisticLockingFailureException) {
            return "Record was updated by another transaction/user, please try again.";
        }

        if (ex == null || ex.getMessage() == null || ex.getMessage().length() <= 0) {
            LOG.warn("No message to display for error: ", ex);
            return "";
        }
        return ex.getMessage();
    }

    @Override
    public String execute() throws Exception {

        if (securityInfoProvider.getCurrentUser() != null) {
            return VALID_SESSION;
        }
        return SUCCESS;
    }

	public String getDevelopment() {
		return ServletActionContext.getServletContext().getInitParameter("development");
	}

}
