package idas.chox.web.actions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;

import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

import idas.chox.core.model.Entity;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.ActionResponse;


public class BaseAction extends ActionSupport implements SessionAware {

    private static final Logger LOG = LoggerFactory.getLogger(BaseAction.class);
    protected ActionResponse actionResponse;
    private String actionResult;
    private String actionError;
    private SecurityInfoProvider securityInfoProvider;
    private Map<String,Object> session;
    private String VALID_SESSION = "validSession";
    private BaseDataService baseDataService;

    public Map<String,Object> getSession() {
    	if(session == null) {
    		session = new HashMap<String, Object>();//TODO session is sometimes null ?!?
        }
        return session;
    }

    @Override
    public void setSession(Map<String,Object> session) {
        this.session = session;
    }

    public boolean isSearchHistory() {
        return session.containsKey("searchHistory");
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

    public boolean getIsAdmin() {
        return securityInfoProvider.getIsCHOXAdmin()
                || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_ADMIN)
                || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH_MNG);
    }

    public boolean getIsCH() {
        return securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH);
    }

    public boolean getIsOp() {
        return securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH_OPR);
    }

    public boolean isPcOnly() {        
        boolean isPc = securityInfoProvider.isInRoleOf(WebUserRole.ROLE_PC);
        boolean isMng = securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG);
        boolean isCH = securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH);
        boolean isUpload = securityInfoProvider.isInRoleOf(WebUserRole.ROLE_UPLOAD);
//        boolean isAdmin = securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CHOX_ADMIN);
        
        return isPc && !isMng && !isCH && !isUpload;
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
            LOG.debug("returning insurerIsUploadEnabled: {}", getAuthenticatedUser().getInsurer().isInvoiceUploadEnabled());
            return getAuthenticatedUser().getInsurer().isInvoiceUploadEnabled();
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
    
    public boolean getInsurerIsSupervisorEnabled() {
        if (getIsCHO()) {
            return false;
        } else if (getIsInsurer()){
            return getAuthenticatedUser().getInsurer().isSupervisorEnable();
        } else {
            return true; //for CHOX admin we return true
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

    public boolean getCanExport() {
        boolean result = true;
        if (getIsInsurer() && securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_USER)) {
            result = !getAuthenticatedUser().getInsurer().isRestrictExport();
        }
        else if (getIsCHO() && securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH_OPR)) {
            result = !getAuthenticatedUser().getChorganisation().isRestrictExport();
        }
        return result;
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
            if (securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG) || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_ADMIN)
                    || securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_USER)) {
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

    public BaseDataService getBaseDataService() {
        return baseDataService;
    }

    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    protected void handleException(Exception ex) {
        if (ex instanceof StaleObjectStateException || ex instanceof HibernateOptimisticLockingFailureException
                || (ex.getCause() != null && ex.getCause() instanceof StaleObjectStateException)) {
            LOG.warn("StaleObjectStateException thrown: {}", ex.getMessage());
        } else if (ex instanceof AccessDeniedException) {
            LOG.error("AccessDeniedException thrown: {}", ex.getMessage());
            throw new AccessDeniedException(ex.getMessage());
        } else {
            LOG.warn("handleException: exception is {} of class '{}'", ex.getMessage(), ex.getClass());
            if (ex.getCause() != null) {
                LOG.warn("     caused by: {}", ex.getCause(), ex.getCause());
            }
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
    
    public void checkVersion(List<? extends Entity> models) throws Exception {
        for (Entity model : models) {
            if (model != null && getSession().containsKey(model.getClass().getSimpleName())) {
                HashMap<String, Integer> map = (HashMap) getSession().get(model.getClass().getSimpleName());
                if (map != null && map.get("version") != null && map.get("id") != null && model.getVersion() != null) {
                    Integer sessionModelVersion = map.get("version");
                    Integer sessionModelId = map.get("id");
                    LOG.debug("Checking version for modelname={} with sessionVersion={}, sessionId={}, modelVersion={}, modelId={}",
                            new Object[]{model.getClass().getSimpleName(), sessionModelVersion, sessionModelId, model.getVersion(), model.getId()});
                    if (model.getId().compareTo(sessionModelId) == 0 && model.getVersion().compareTo(sessionModelVersion) != 0) {
                        LOG.info("{} model is updated by another user. session version={}, database version={}. Throwing staleObject Exception.",
                                new Object[]{model.getClass().getSimpleName(), sessionModelVersion, model.getVersion()});
//                        StaleObjectStateException ex = new StaleObjectStateException(model.getClass().getSimpleName().concat("Version"), model.getId());
                        Exception ex = new Exception("Record was updated by another transaction/user, please try again."
                                , new StaleObjectStateException(model.getClass().getSimpleName().concat("Version"), model.getId()));
                        // before throwing exception update model so that next time when the user save the model they will not get stale object exception.
                        updateModelInSession(models);
                        throw ex;
                    }
                }
            }
        }
    }

    /* This will do force update the model in session*/
    public void updateModelInSession(List<? extends Entity> models) {
        for (Entity model : models) {
            if (model != null &&  model.getVersion() != null && model.getId() != null) {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("version", model.getVersion());
                map.put("id", model.getId());
                getSession().put(model.getClass().getSimpleName(), map);
                LOG.debug("session updated for model name={}, modelVersion={} modelId={}",
                        new Object[]{model.getClass().getSimpleName(), model.getVersion(), model.getId()});
            }
        }
    }
    
    /* This will put the model in session if it does not already exists or having differnt id than the one already in the session
     in case of different id in the session for the same class it will replace with the new model*/
    public void addModelToSession(List<? extends Entity> models) {
        for (Entity model : models) {
            if (model != null && !getSession().containsKey(model.getClass().getSimpleName())) {
                LOG.debug("model is not in session and will be added to session");
                updateModelInSession(Arrays.asList(model));
            } else if (model != null){
                HashMap<String, Integer> map = (HashMap) getSession().get(model.getClass().getSimpleName());

                if (model.getId() != null && map.get("id").compareTo(model.getId()) != 0) {
                    LOG.debug("model with same name exists but different Id, replacing with new model");
                    updateModelInSession(Arrays.asList(model));
                }
            }
        }
    }


    public Integer getModelIdFromSession(Class model) {
        if (getSession().containsKey(model.getSimpleName())) {
            HashMap<String, Integer> map = (HashMap) getSession().get(model.getSimpleName());
            LOG.debug("{} model is accessed from session and id is {}", model.getSimpleName(), map.get("id"));
            return map.get("id");
        } else {
            LOG.info("Claim is not in session and returing null");
            return null;
        }
    }
}
