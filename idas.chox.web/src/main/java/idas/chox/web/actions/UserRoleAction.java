package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.common.OrganisationType;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.WebUserUserRole;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import idas.chox.web.viewdata.UserroleViewData;

public class UserRoleAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(UserRoleAction.class);

    private List<UserroleViewData> userroles;
    private int webUserId;
    private int organisationTypeId;
    private int webUserUserRoleId;
    private int webUserRoleId;
    private int objectId;
    private String webUserRoleCode;
    private AdminUserService adminUserService;
    private WebUserUserRoleService webUserUserRoleService;

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER"})
    public String doRenderActionPage() {
        LOG.debug("doRenderActionPage() called for user: {}", webUserId);
        return SUCCESS;
    }

    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(userroles);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting claimhandlers to json string.");
        }
        LOG.debug("Returning user roles: {}", jsonString);
        return "{totalCount:" + this.userroles.size() + ",results:" + jsonString + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

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

    public WebUserUserRoleService getWebUserUserRoleService() {
        return webUserUserRoleService;
    }

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getUserRoles() {
        LOG.debug("Getting user roles for user: {}", webUserId);
        try {

            userroles = new ArrayList<>();

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

    public List<IdLookupItem> getAvailableUserRoles() {
        LOG.debug("Getting available user roles for user {} ({})", webUserId, organisationTypeId);
        LOG.debug("ObjectId = {}", objectId);
        Insurer insurer = adminUserService.getUser(webUserId).getInsurer();
        if (insurer != null) {
            return adminUserService.getAvailableUserRoles(organisationTypeId, webUserId,
                    insurer.isWorkgroupEnable(), insurer.isClaimOwnershipEnable(),
                    insurer.isFnolEnable(), insurer.isEngineersEnable(), insurer.isInvoiceUploadEnabled(), insurer.isSupervisorEnable(), getIsChoxAdmin());
        }

        Chorganisation cho = adminUserService.getUser(webUserId).getChorganisation();

        if (cho != null) {
            return adminUserService.getAvailableUserRoles(organisationTypeId, webUserId,
                    getInsurerIsWorkgroupEnabled(), getChoIsClaimOwnershipEnabled(),
                    getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), isInsurerUploadEnabled(), cho.isSupervisorEnable(), getIsChoxAdmin());
        }

        return adminUserService.getAvailableUserRoles(organisationTypeId, webUserId,
                getInsurerIsWorkgroupEnabled(), getChoIsClaimOwnershipEnabled(),
                getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), isInsurerUploadEnabled(), this.getIsSupervisorEnabled(), getIsChoxAdmin());
    }

    public String getAllAvailableUserRoles() {
        LOG.debug("Getting all available user roles...");
        Set<WebUserRole> webUserRoles;
        LOG.debug("Getting available user roles for user {}", webUserId);
        LOG.debug("ObjectId = {}", objectId);
        try {
            Insurer insurer = adminUserService.getUser(webUserId).getInsurer();
            Chorganisation cho = adminUserService.getUser(webUserId).getChorganisation();
            if (insurer != null) {
                webUserRoles = adminUserService.getAllAvailableUserRoles(2,
                        insurer.isWorkgroupEnable(), insurer.isClaimOwnershipEnable(),
                        insurer.isFnolEnable(), insurer.isEngineersEnable(), insurer.isInvoiceUploadEnabled(), insurer.isSupervisorEnable(), getIsChoxAdmin());
            } else if (cho != null) {
                webUserRoles = adminUserService.getAllAvailableUserRoles(3,
                        getInsurerIsWorkgroupEnabled(), getInsurerIsClaimOwnershipEnabled(),
                        getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), isInsurerUploadEnabled(), cho.isSupervisorEnable(), getIsChoxAdmin());
            } else {
                webUserRoles = adminUserService.getAllAvailableUserRoles(1,
                        getInsurerIsWorkgroupEnabled(), getInsurerIsClaimOwnershipEnabled(),
                        getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), isInsurerUploadEnabled(), false, true);
            }

            userroles = new ArrayList<>(webUserRoles.size());

            for (WebUserRole webUserRole : webUserRoles) {
                userroles.add(new UserroleViewData(webUserRole));
            }
        } catch (Exception ex) {
            handleException(ex);
            LOG.debug("Error getting all available user roles: {}", ex.getMessage());
            return ERROR;
        }

        LOG.debug("Found {} user roles: {}", userroles.size(), userroles);

        return SUCCESS;
    }

    public String getAvailableUserRolesForTask() {
        LOG.trace("Getting all available user roles for task assignment...");
        Set<WebUserRole> webUserRoles;
        LOG.trace("Getting available user roles for user {}", webUserId);
        LOG.trace("ObjectId = {}", objectId);
        try {
            Insurer insurer = adminUserService.getUser(webUserId).getInsurer();
            Chorganisation cho = adminUserService.getUser(webUserId).getChorganisation();
            if (insurer != null) {
                webUserRoles = adminUserService.getAllAvailableUserRoles(2,
                        insurer.isWorkgroupEnable(), insurer.isClaimOwnershipEnable(),
                        insurer.isFnolEnable(), insurer.isEngineersEnable(), insurer.isInvoiceUploadEnabled(), insurer.isSupervisorEnable(), getIsChoxAdmin(), true);
            } else if (cho != null) {
                webUserRoles = adminUserService.getAllAvailableUserRoles(3,
                        getInsurerIsWorkgroupEnabled(), getInsurerIsClaimOwnershipEnabled(),
                        getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), isInsurerUploadEnabled(), cho.isSupervisorEnable(), getIsChoxAdmin(), true);
            } else {
                webUserRoles = adminUserService.getAllAvailableUserRoles(3,
                        getInsurerIsWorkgroupEnabled(), getInsurerIsClaimOwnershipEnabled(),
                        getInsurerIsFnolEnabled(), getInsurerIsEngineersEnabled(), isInsurerUploadEnabled(), false, true, true);
            }

            userroles = new ArrayList<>(webUserRoles.size());

            for (WebUserRole webUserRole : webUserRoles) {
                userroles.add(new UserroleViewData(webUserRole));
            }
        } catch (Exception ex) {
            handleException(ex);
            LOG.debug("Error getting all available user roles for user with id= {}: {}", webUserId, ex.getMessage());
            return ERROR;
        }

        LOG.debug("Found {} user roles: {}", userroles.size(), userroles);

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER"})
    public String addNewWebUserRoleMapping() {
        LOG.debug("Adding user role '{}' to user '{}'", webUserRoleId, webUserId);

        if (webUserId > 0 && webUserRoleId > 0) {

            try {
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
//                    throw new Exception("Record was updated by another transaction/user, please try again.",
//                            new StaleObjectStateException(WebUserUserRole.class.getSimpleName().concat("Version"), 0));
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
        List<IdLookupItem> availableRoles = adminUserService.getAvailableUserRoles(orgType, webUserId);
        LOG.debug("We have {} roles available:", availableRoles.size());
        for (IdLookupItem lu : availableRoles) {
            LOG.debug("Role available: {} - '{}'", lu.getId(), lu.getName());
            if (lu.getId() == webUserRoleId) {
                return true;
            }
        }
        return false;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER"})
    public String removeWebUserRoleMapping() {
        LOG.debug("Removing user role '{}' to user '{}'", webUserRoleId, webUserId);

        if (this.webUserUserRoleId > 0) {

            try {
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
                WebUserUserRole webUserUserRole = this.webUserUserRoleService.getWebUserUserRole(webUserUserRoleId);
                if (webUserUserRole != null) {
                    webUserUserRoleService.deleteWebUserUserRole(webUserUserRole);
                } else {
                    throw new Exception("Record was updated by another transaction/user, please try again.",
                            new StaleObjectStateException(WebUserUserRole.class.getSimpleName().concat("Version"), 0));
                }
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
            WebUserUserRole webUserUserRole = this.webUserUserRoleService.getWebUserUserRole(webUserUserRoleId);
            if (webUserUserRole != null) {
                ActionResponse response = adminUserService.validateRoleToBeDeleted(this.webUserId, this.webUserRoleCode);
                setActionResponse(response);
            }
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
