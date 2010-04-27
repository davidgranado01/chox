package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import org.springframework.security.annotation.Secured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.services.LookupService;
import idas.chox.service.admin.AdminUserService;
import idas.chox.web.viewdata.UserViewData;
import idas.chox.service.ActionResponse;
import org.springframework.security.AccessDeniedException;

public class UserAction extends BaseAction implements ModelDriven<WebUser>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(UserAction.class);

    private List<UserViewData> users = new ArrayList<UserViewData>();
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

    public boolean getIsNew() {

        if (objectId != null && !objectId.equalsIgnoreCase("")) {
            if (Integer.valueOf(objectId) > 0) {
                return false;
            }
        }
        return true;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG", "ROLE_CHO_MNG"})
    public String doRenderActionPage() {
        try {
            if (Integer.parseInt(objectId) != -1) {
                // Need to check that the web user (objectId) belongs to our organisation
                // this is to prevent parameter hacking
                WebUser user = adminUserService.getUser(Integer.parseInt(objectId));
                if ((getUserOrganisationType() == 2 && getUserOrganisationId() != user.getInsurer().getId())
                        || (getUserOrganisationType() == 3 && getUserOrganisationId() != user.getChorganisation().getId())) {
                    throw new AccessDeniedException("Trying to view a user not of my organisation (POSSIBLE HACK ATTEMPT)");
                }
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
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
        return "{totalCount:" + this.users.size() + ",results:" + jObject.toString() + "}";
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
        
        try {
            
            List<WebUser> userData = adminUserService.getUsers(organisationId, organisationTypeId, userRoleId);

            for (WebUser h : userData) {
                if (userRoleId > 0) {
                    if (isSelectedRoleExist(h.getRoles(), userRoleId)) {
                        users.add(new UserViewData(h));
                    }
                } else {
                    users.add(new UserViewData(h));
                }
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    private boolean isSelectedRoleExist(Set roles, int selectedRole) {
        boolean isExist = false;
        try {
            
            Iterator it = roles.iterator();

            while (it.hasNext()) {
                WebUserRole webUserrole = (WebUserRole) it.next();
                if (webUserrole.getId() == selectedRole) {
                    isExist = true;
                    break;
                }
            }
            
        } catch (Exception ex) {
            handleException(ex);
        }
        return isExist;
    }

    public boolean getIsWorkgroupEnabled() {
        boolean isEnable = false;
        if (model.getInsurer() != null) {
            isEnable = model.getInsurer().isWorkgroupEnable();
        }
        return isEnable;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG", "ROLE_CHO_MNG"})
    public String updateUserDetail() throws Exception {

        try {
            LOG.debug("getUserOrganisationType(): {}", getUserOrganisationType());
            LOG.debug("getUserOrganisationId(): {}", getUserOrganisationId());
            LOG.debug("this.insurerId: {}", this.insurerId);
            LOG.debug("this.supplierId: {}", this.supplierId);
            LOG.debug("model.isInsurer(): {}", model.isInsurer());
            if ((getUserOrganisationType() == 2 && (  (this.insurerId == -1 && (!model.isInsurer() || model.getInsurer().getId() != getUserOrganisationId()))
                                                    ||(this.insurerId != -1 && (this.insurerId != getUserOrganisationId()))))
                    || (getUserOrganisationType() == 3 && ((model.isInsurer() || (this.supplierId == -1 && model.getChorganisation().getId() != getUserOrganisationId()))
                    || (this.supplierId != -1 && this.supplierId != getUserOrganisationId())))) {
                throw new AccessDeniedException("Trying to create a user not of my organisation (POSSIBLE HACK ATTEMPT)");
            }
            ActionResponse response;

            if (getIsNew()) {

                response = adminUserService.doAddNewUser(model, this.insurerId, this.supplierId, this.organisationTypeId);

            } else {

                response = adminUserService.updateUser(model);

            }

            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String updateUserPassword() throws Exception {
        LOG.debug("Updating user password.");
        try {
            LOG.debug("getUserOrganisationType()={}", getUserOrganisationType());
            LOG.debug("model.isInsurer()={}", model.isInsurer());
            LOG.debug("model.getId()={}", model.getId());
            LOG.debug("model.getChorganisation()={}", model.getChorganisation());
            LOG.debug("getUserOrganisationId()={}", getUserOrganisationId());
            LOG.debug("getAuthenticatedUser().getId()={}", getAuthenticatedUser().getId());
            LOG.debug("getRoleTypeForHelpFile()={}", getRoleTypeForHelpFile());
            if ((getUserOrganisationType() == 2 && (!model.isInsurer() || model.getInsurer().getId() != getUserOrganisationId()))
                    || (getUserOrganisationType() == 3 && (model.isInsurer() || (model.getChorganisation() != null && model.getChorganisation().getId() != getUserOrganisationId())))) {
                LOG.debug("Failed access validation - throwing AccessDeniedException");
                throw new AccessDeniedException("Trying to update the password of a user not of my organisation (or not me) (POSSIBLE HACK ATTEMPT)");
            }
            LOG.debug("Passed access validation");

            ActionResponse response = adminUserService.updateUserPassword(model);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        
        return SUCCESS;
    }

    public String triggerUserAccountStatus() throws Exception {

        try {

            ActionResponse response = adminUserService.triggerUserStatus(model);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String triggerPasswordExpiredStatus() {

        try {

            ActionResponse response = adminUserService.triggerPasswordExpiredStatus(model);
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
    // </editor-fold>
}
