package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.WebUser;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import idas.chox.web.viewdata.UserViewData;

public class UserAction extends BaseAction implements ModelDriven<WebUser>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(UserAction.class);
    private List<UserViewData> users = new ArrayList<>();
    private int organisationTypeId = -1;
    private int organisationId = -1;
    private int userRoleId = -1;
    private String objectId;
    private WebUser model;
    private Integer insurerId = -1;
    private Integer supplierId = -1;
    private Integer tabIndex;
    private AdminUserService adminUserService;
    private LookupService lookupService;
    private ChorganisationService chorganisationService;
    private InsurerService insurerService;
    private ClaimService claimService;
    private int start;
    private int limit;
    private String sort;
    private String dir;
    private int totalCount;
    private boolean activeUsersOnly;

    public String getDir() {
        return dir;
    }

    public int getMinPasswordLength() {
        int minPasswordLength = 8;
        int orgId = getCurrentUserOrganisationId();
        LOG.debug("Getting minimum password length");
        if (organisationTypeId == 2 && model != null && model.getInsurer() != null) { // Insurer
            LOG.debug("Insurer user: getting minimum password length for insurerId={}", model.getInsurer().getId());
            minPasswordLength = model.getInsurer().getMinimumPasswordLength();
        }
        else if (organisationTypeId == 3 && model != null && model.getChorganisation() != null) { // CHO
            LOG.debug("CHO user: getting minimum password length for supplierId={}", model.getChorganisation().getId());
            minPasswordLength = model.getChorganisation().getMinimumPasswordLength();
        }
        else if (organisationTypeId == 2 && insurerService != null && orgId != 1) {
            LOG.debug("Insurer user from orgId={}", orgId);
            minPasswordLength = insurerService.getInsurer(orgId).getMinimumPasswordLength();
        }
        else if (organisationTypeId == 3 && chorganisationService != null && orgId != 1) {
            LOG.debug("CHO user from orgId={}", orgId);
            minPasswordLength = chorganisationService.getChorganisation(orgId).getMinimumPasswordLength();
        }else {
            LOG.debug("CHOX Admin user ?: organisationTypeId={}, orgId={}", organisationTypeId, orgId);
            LOG.debug("insurerId={}, supplierId={}", insurerId, supplierId);
        }

        LOG.debug("Returning minPasswordLength={}", minPasswordLength);
        
        return minPasswordLength;
    }
    
    public String getUserPasswordMessage() {
        LOG.debug("Getting user password message for orgtype={}, org={}", organisationTypeId, organisationId);
        int minPasswordLength = 8;
        if (organisationTypeId == 2 && insurerService != null) {
            minPasswordLength = insurerService.getInsurer(organisationId).getMinimumPasswordLength();
        }
        else if (organisationTypeId == 3 && chorganisationService != null) {
            Chorganisation cho = chorganisationService.getChorganisation(organisationId);
            LOG.debug("CHO is {}", cho.getName());
            minPasswordLength = cho.getMinimumPasswordLength();
        }

        LOG.debug("minPasswordLength={}", minPasswordLength);
        getActionResponse().AssignMessageResult("" + minPasswordLength);

        return SUCCESS;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public boolean isActiveUsersOnly() {
        return activeUsersOnly;
    }

    public void setActiveUsersOnly(boolean activeUsersOnly) {
        this.activeUsersOnly = activeUsersOnly;
    }

    public boolean getIsNew() {

        if (objectId != null && !objectId.equalsIgnoreCase("")) {
            if (Integer.valueOf(objectId) > 0) {
                return false;
            }
        }
        return true;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER"})
    public String doRenderActionPage() {
        updateModelInSession(Arrays.asList(model));
        return SUCCESS;
    }

    @Override
    public WebUser getModel() {
        return model;
    }

    public void setModel(WebUser model) {
        this.model = model;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.users);
        return "{totalCount:" + totalCount + ",results:" + jObject.toString() + "}";
    }

    public int getCurrentUserOrganisationId() {
        return getUserOrganisationId();
    }

    @Override
    public void prepare() throws Exception {
        try {
            model = new WebUser();
            model.setClaimHandler(false);

            if (objectId != null && !objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminUserService.getUser(Integer.valueOf(objectId));
                    addModelToSession(Arrays.asList(model));
                }
            }
        } catch (Exception ex) {
            handleException(ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public List getInsurers() {
        List insurers = this.lookupService.getInsurers();
        return insurers;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public List getSuppliers() {
        List suppliers = this.lookupService.getAllSuppliers();
        return suppliers;
    }

    public Integer getInsurerId() {

        if (getIsChoxAdmin()) {
            insurerId = getAuthenticatedUser().getInsurer().getId();
        }
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        this.insurerId = insurerId;
    }

    public Integer getSupplierId() {
        if (!getIsChoxAdmin()) {
            supplierId = getAuthenticatedUser().getChorganisation().getId();
        }
        return supplierId;
    }

    public int getOrganisationTypeId() {
        return organisationTypeId;
    }

    public void setOrganisationTypeId(int organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
    }

    public int getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(int organisationId) {
        this.organisationId = organisationId;
    }

    public int getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    public String getGridViewUser() {

        if((!getIsChoxAdmin() && getUserOrganisationType() != organisationTypeId)
                || (!getIsChoxAdmin() && getUserOrganisationId() != organisationId)) {
            LOG.warn("Not in correct organisation to view data: organisationTypeId={} ({}), organisationId={} ({})",
                    new Object[]{organisationTypeId, getUserOrganisationType(), organisationId, getUserOrganisationId()});
            throw new AccessDeniedException("You do not have the correct access role to view the requested data. You will now be logged out.");
        }
        try {

            SearchResult searchResult = adminUserService.getUsers(organisationId, organisationTypeId, userRoleId, start, limit, sort, dir, activeUsersOnly);
            List<WebUser> userData = searchResult.getResult();
            totalCount = searchResult.getTotalCount();
            for (WebUser h : userData) {
                users.add(new UserViewData(h));
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }


    
    public boolean getIsWorkgroupEnabled() {
        boolean isEnable = false;
        if (model.getInsurer() != null) {
            isEnable = model.getInsurer().isWorkgroupEnable();
        }
        return isEnable;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER"})
    public String updateUserDetail() throws Exception {

        try {
            LOG.debug("getUserOrganisationType(): {} getUserOrganisationId(): {} this.insurerId: {} this.supplierId: {} model.isAnInsurer(): {}", 
                    new Object[] {getUserOrganisationType(), getUserOrganisationId(), this.insurerId, this.supplierId, model.isAnInsurer()});
            
            if ((getUserOrganisationType() == 2 && ((this.insurerId == -1 && (!model.isAnInsurer() || model.getInsurer().getId().intValue() != getUserOrganisationId()))
                    || (this.insurerId != -1 && (this.insurerId != getUserOrganisationId()))))
                    || (getUserOrganisationType() == 3 && ((model.isAnInsurer() || (this.supplierId == -1 && model.getChorganisation().getId().intValue() != getUserOrganisationId()))
                    || (this.supplierId != -1 && this.supplierId != getUserOrganisationId())))) {
                throw new AccessDeniedException("Trying to create a user not of my organisation (POSSIBLE HACK ATTEMPT)");
            }
            
            checkVersion(Arrays.asList(model));
            
            ActionResponse response;
            
            if (getIsNew()) {
                response = adminUserService.doAddNewUser(model, this.insurerId, this.supplierId, this.organisationTypeId);
            } else if (model.getStatus()){
                // CHOX-313: if user was previously inactive, we need to clear the last login date
                WebUser user = adminUserService.getUser(model.getId());
                if (!user.getStatus()) {
                    model.setLastLoginDate(null);
                }
                response = adminUserService.updateUser(model);
            } else { // user is in-active - check no open claims
                if (claimService.isUserHasOpenClaim(model.getId(), model.getInsurer() != null ? true : false)) {
                    response = new ActionResponse();
                    response.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "This user currently has assigned claims. Please reassign these claims before de-activating this user account");
                } else {
                    response = adminUserService.updateUser(model);
                }
            }
            updateModelInSession(Arrays.asList(model));
            setActionResponse(response);
        
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER"})
    public String updateUserPassword() throws Exception {
        LOG.debug("Updating user password.");
        try {
            LOG.debug("getUserOrganisationType()={} model.isAnInsurer()={} model.getId()={} model.getChorganisation()={} getUserOrganisationId()={} getAuthenticatedUser().getId()={} getRoleTypeForHelpFile()={}", 
                    new Object[]{getUserOrganisationType(), model.isAnInsurer(), model.getId(), model.getChorganisation(), getUserOrganisationId(), getAuthenticatedUser().getId(), getRoleTypeForHelpFile()});

            if ((getUserOrganisationType() == 2 && (!model.isAnInsurer() || model.getInsurer().getId().intValue() != getUserOrganisationId()))
                    || (getUserOrganisationType() == 3 && (model.isAnInsurer() || (model.getChorganisation() == null || model.getChorganisation().getId().intValue() != getUserOrganisationId())))) {
                    LOG.warn("Access Denied for user trying to update password: getUserOrganisationType()={} model.isAnInsurer()={} model.getId()={} model.getChorganisation()={} getUserOrganisationId()={} getAuthenticatedUser().getId()={} getRoleTypeForHelpFile()={}", 
                        new Object[]{getUserOrganisationType(), model.isAnInsurer(), model.getId(), model.getChorganisation(), getUserOrganisationId(), getAuthenticatedUser().getId(), getRoleTypeForHelpFile()});
                throw new AccessDeniedException("Trying to update the password of a user not of my organisation (or not me) (POSSIBLE HACK ATTEMPT)");
            }
            LOG.debug("Passed access validation");

            checkVersion(Arrays.asList(model));
            ActionResponse response = adminUserService.updateUserPassword(model);
            updateModelInSession(Arrays.asList(model));
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER"})
    public String triggerUserAccountStatus() throws Exception {

        if ((getUserOrganisationType() == 2 && (!model.isAnInsurer() || model.getInsurer().getId().intValue() != getUserOrganisationId()))
                || (getUserOrganisationType() == 3 && (model.isAnInsurer() || (model.getChorganisation() == null || model.getChorganisation().getId().intValue() != getUserOrganisationId())))) {
            LOG.debug("Failed access validation - throwing AccessDeniedException");
            throw new AccessDeniedException("Trying to update the triggerUserAccountStatus of a user not of my organisation (or not me) (POSSIBLE HACK ATTEMPT)");
        }
        try {
            checkVersion(Arrays.asList(model));
            ActionResponse response = adminUserService.triggerUserStatus(model);
            updateModelInSession(Arrays.asList(model));
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_USER", "ROLE_CHO_USER"})
    public String triggerPasswordExpiredStatus() {

        if ((getUserOrganisationType() == 2 && (!model.isAnInsurer() || model.getInsurer().getId().intValue() != getUserOrganisationId()))
                || (getUserOrganisationType() == 3 && (model.isAnInsurer() || (model.getChorganisation() == null || model.getChorganisation().getId().intValue() != getUserOrganisationId())))) {
            LOG.debug("Failed access validation - throwing AccessDeniedException");
            throw new AccessDeniedException("Trying to update the triggerPasswordExpiredStatus of a user not of my organisation (or not me) (POSSIBLE HACK ATTEMPT)");
        }
        try {
            checkVersion(Arrays.asList(model));
            ActionResponse response = adminUserService.triggerPasswordExpiredStatus(model);
            updateModelInSession(Arrays.asList(model));
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    // </editor-fold>
}
