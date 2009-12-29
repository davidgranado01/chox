package idas.chox.web.actions;

import com.opensymphony.xwork2.ActionSupport;
import idas.chox.core.model.WebUserRole;
import idas.chox.web.security.AcegiPrincipal;
import idas.chox.service.security.PermissionedUser;
import idas.chox.web.viewdata.ActionResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;

public class BaseAction extends ActionSupport {

    protected static Log logger = LogFactory.getLog("chox");
    protected PermissionedUser user;
    protected ActionResponse actionResponse;

    @AcegiPrincipal
    public void setAuthenticatedUser(PermissionedUser user) {
        this.user = user;
    }

    public PermissionedUser getAuthenticatedUser() {

        if (user == null) {
            Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
            if (currentUser != null) {
                user = (PermissionedUser) currentUser.getPrincipal();
            }

        }
        return user;
    }

    

    public boolean getIsCHO() {
        return getAuthenticatedUser().getIsCHO();
    }

    public boolean getIsInsurer() {
        return getAuthenticatedUser().getIsINS();
    }

    public boolean getIsChoxAdmin() {

        boolean isChoxAdmin = false;

        if (getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_CHOX)) {
            isChoxAdmin = true;
        }

        return isChoxAdmin;
    }

    public int getOrganisationId() {

        int orgId = -1;
        if (getAuthenticatedUser().getIsINS()) {
            orgId = getAuthenticatedUser().getUser().getInsurer().getId();
        } else if (getAuthenticatedUser().getIsCHO()) {
            orgId = getAuthenticatedUser().getUser().getChorganisation().getId();
        }

        return orgId;

    }

    public String getCurrentUserDesc() {
        String logInUserDesc = user.getUser().getFirstName() + " " + user.getUser().getLastName();
        String strOrgType = "";

        if (user.getIsCHO()) {
            strOrgType = user.getUser().getChorganisation().getName();
        } else if (user.getIsINS()) {
            strOrgType = user.getUser().getInsurer().getName();
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
            if (getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_INS_MNG)) {
                iRoleType = 2;
            }
        }

        if (getIsCHO()) {
            iRoleType = 3;
            if (getAuthenticatedUser().isInRoleOf(WebUserRole.ROLE_CH_MNG)) {
                iRoleType = 4;
            }
        }

        return iRoleType;
    }
}
