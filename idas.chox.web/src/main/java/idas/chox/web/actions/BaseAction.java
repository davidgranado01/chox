package idas.chox.web.actions;

import com.opensymphony.xwork2.ActionSupport;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.service.ActionResponse;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseAction extends ActionSupport {

    private SecurityInfoProvider securityInfoProvider;
    protected static Log logger = LogFactory.getLog("chox");   
    protected ActionResponse actionResponse;
 
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

    public int getUserOrganisationType(){
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

    public ActionResponse getActionResponse() {
        if (actionResponse == null) {
            actionResponse = new ActionResponse();
        }
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    public String getActionResponseString()
    {
       JSONObject jsonObject = JSONObject.fromObject(getActionResponse());
       return jsonObject.toString();
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

    protected void handleException(Object source,Exception ex) {
        logger.error(ex.getMessage());
        getActionResponse().AddError(ex.getMessage());
    }

    /**
     * @param securityInfoProvider the securityInfoProvider to set
     */
    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }
}
