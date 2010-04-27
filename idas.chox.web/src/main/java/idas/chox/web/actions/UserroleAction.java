package idas.chox.web.actions;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import idas.chox.web.viewdata.UserroleViewData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONArray;
import org.springframework.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;

public class UserroleAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(UserroleAction.class);

    private List<UserroleViewData> userroles;
    private int webUserId;
    private int organisationTypeId;
    private int webUserUserRoleId;
    private int webUserRoleId;
    private String webUserRoleCode;
    private AdminUserService adminUserService;

    public String doRenderActionPage() {
        LOG.debug("doRenderActionPage() called for user: {}", webUserId);
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.userroles);
        return "{totalCount:" + this.userroles.size() + ",results:" + jObject.toString() + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getOrganisationTypeId() {
        return organisationTypeId;
    }

    public void setOrganisationTypeId(int organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
    }

    public int getWebUserRoleId() {
        return webUserRoleId;
    }

    public void setWebUserRoleId(int webUserRoleId) {
        this.webUserRoleId = webUserRoleId;
    }

    public int getWebUserUserRoleId() {
        return webUserUserRoleId;
    }

    public void setWebUserUserRoleId(int webUserUserRoleId) {
        this.webUserUserRoleId = webUserUserRoleId;
    }

    public String getWebUserRoleCode() {
        return webUserRoleCode;
    }

    public void setWebUserRoleCode(String webUserRoleCode) {
        this.webUserRoleCode = webUserRoleCode;
    }

    public int getWebUserId() {
        return webUserId;
    }

    public void setWebUserId(int webUserId) {
        this.webUserId = webUserId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getUseroles() {
        LOG.debug("Getting user roles for user: {}", webUserId);
        try {

            userroles = new ArrayList<UserroleViewData>();

            List<WebUserUserRole> userroleData = adminUserService.getMappedUserRole(webUserId);

            for (WebUserUserRole h : userroleData) {
                if (!h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CHO) && !h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_INS) && !h.getWebUserRole().getName().equalsIgnoreCase(WebUserRole.ROLE_CHOX)) {
                    userroles.add(new UserroleViewData(h));
                }
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public List<IdLookupItem> getAvailableUserroles() {
          LOG.debug("Getting available user roles for user {} ({})", webUserId, organisationTypeId);
      return adminUserService.getAvailableUserroles(organisationTypeId, webUserId);
    }

// For some reason the following line causes the add/remove role panel to be displayed empty
//    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG", "ROLE_CHO_MNG"})
    public String addNewWebUserRoleMapping() {
        LOG.debug("Adding user role '{}' to user '{}'", webUserRoleId, webUserId);

        if (webUserId > 0 && webUserRoleId > 0) {

            try {
                // As Spring/ACEGI security is commented out (above), we'll check manually that we are an admin
                if (!getIsAdmin()) {
                    throw new AccessDeniedException("Non admin role trying to add a new role (POSSIBLE HACK ATTEMPT)");
                }
                // Need to check that that the user we are attaching the role to is one of our users
                // this is to prevent parameter hacking
                LOG.debug("Checking access to addNewWebUserRoleMapping for current user");
                WebUser user = adminUserService.getUser(webUserId);
                LOG.debug("User who me are adding roles to is: {} ('{}')", webUserId, user.getDisplayName());
                if ((getUserOrganisationType() == 2 && (user.getInsurer() == null || getUserOrganisationId() != user.getInsurer().getId()))
                         || (getUserOrganisationType() == 3 && (user.getChorganisation() == null || getUserOrganisationId() != user.getChorganisation().getId()))) {
                    throw new AccessDeniedException("Trying to add a role to a user not of my organisation (POSSIBLE HACK ATTEMPT)");
                }
                // Check that the role is one we can add
                if (!isRoleAvailable(webUserRoleId, OrganisationType.getOrganisationTypeId(user.getOrganisationType()))) {
                    throw new AccessDeniedException("Trying to add a role not available (POSSIBLE HACK ATTEMPT)");
                }
                ActionResponse response = adminUserService.addNewWebUserRoleMapping(webUserId, webUserRoleId);
                setActionResponse(response);

            } catch (Exception ex) {
                LOG.debug("Handling exception: '{}'", ex.getMessage());
                handleException(ex);
                return ERROR;
            }
        }

        return SUCCESS;
    }

    private boolean isRoleAvailable(int webUserRoleId, int orgType) {
        LOG.debug("Is role {} available to this user?", webUserRoleId);
        List<IdLookupItem> availableRoles = adminUserService.getAvailableUserroles(orgType, webUserId);
        LOG.debug("We have {} roles available:", availableRoles.size());
        for (Iterator<IdLookupItem> i = availableRoles.iterator(); i.hasNext( ); ) {
            IdLookupItem lu = i.next();
            LOG.debug("Role available: {} - '{}'", lu.getId(), lu.getName());
            if (lu.getId() == webUserRoleId)
                return true;
        }
        return false;
    }

// For some reason the following line causes the add/remove role panel to be displayed empty
//    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG", "ROLE_CHO_MNG"})
    public String removeWebUserRoleMapping() {
        LOG.debug("Removing user role '{}' to user '{}'", webUserRoleId, webUserId);

        if (this.webUserUserRoleId > 0) {

            try {
                // As Spring/ACEGI security is commented out (above), we'll check manually that we are an admin
                if (!getIsAdmin()) {
                    throw new AccessDeniedException("Non admin role trying to remove a new role (POSSIBLE HACK ATTEMPT)");
                }
                 // Need to check that that the user we are attaching the role to is one of our users
                 // this is to prevent parameter hacking
                 LOG.debug("Checking access to removeNewWebUserRoleMapping for current user");
                 WebUser user = adminUserService.getUser(webUserId);
                 LOG.debug("User who me are removing roles from is: {} ('{}')", webUserId, user.getDisplayName());
                 if ((getUserOrganisationType() == 2 && (user.getInsurer() == null || getUserOrganisationId() != user.getInsurer().getId()))
                         || (getUserOrganisationType() == 3 && (user.getChorganisation() == null || getUserOrganisationId() != user.getChorganisation().getId()))) {
                     LOG.debug("Throwing AccessDeniedException");
                     throw new AccessDeniedException("Trying to remove a role to a user not of my organisation (POSSIBLE HACK ATTEMPT)");
                 }
                 ActionResponse response = adminUserService.deleteWebUserRoleMapping(this.webUserUserRoleId);
                 setActionResponse(response);
            } catch (Exception ex) {
                LOG.debug("Handling exception: '{}'", ex.getMessage());
                handleException(ex);
                return ERROR;
            }

        }

        return SUCCESS;

    }

    public String checkRoleAllowToDelete() {

        try {

            ActionResponse response = adminUserService.ValidateRoleToBeDeleted(this.webUserId, this.webUserRoleCode);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }
    // </editor-fold>
}