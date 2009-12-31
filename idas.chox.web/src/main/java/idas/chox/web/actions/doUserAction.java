package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WebUserUserRoleService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.PermissionedUser;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminUserService;
import java.util.List;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.providers.encoding.PasswordEncoder;

public class doUserAction extends BaseAction implements ModelDriven<WebUser>, Preparable {

    private String objectId;
    private WebUser model;
    private String organisationTypeId;
    private List insurers;
    private List suppliers;
    private Integer insurerId = -1;
    private Integer supplierId = -1;
    private AdminUserService adminUserService;
    private Integer tabIndex;

    public void setAdminUserService(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }
    private UserService userService;
    private LookupService lookupService;
    private InsurerService insurerService;
    private PermissionedUser currentUser = getAuthenticatedUser();

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public void prepare() throws Exception {
        if (Integer.valueOf(objectId) <= 0) {
            model = new WebUser();
            model.setClaimHandler(false);
        } else {
            model = userService.getUsers(Integer.valueOf(objectId));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    
    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean getIsNew() {
        if (Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getOrganisationTypeId() {
        return organisationTypeId;
    }

    public void setOrganisationTypeId(String organisationTypeId) {
        this.organisationTypeId = organisationTypeId;
    }

    public boolean getIsWorkgroupEnabled() {
        boolean isEnable = false;
        if (model.getInsurer() != null) {
            Insurer insurer = insurerService.getInsurer(model.getInsurer().getId());
            isEnable = insurer.isWorkgroupEnable();
        }
        return isEnable;
    }

    public int getCurrentUserOrganisationId() {
        return getUserOrganisationId();
    }

    public WebUser getModel() {
        return model;
    }

    public void setModel(WebUser model) {
        this.model = model;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ACTION : ADD OR EDIT USER">
    public String updateModel() throws Exception {

        try {

            ActionResponse response;
            
            if (getIsNew()) {
                
                response = adminUserService.doAddNewUser(model, this.insurerId, this.supplierId, this.organisationTypeId);

            } else {
                
                response = adminUserService.updateUser(model);

            }

            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public List getInsurers() {
        insurers = this.lookupService.getInsurers();
        return insurers;
    }

    public List getSuppliers() {
        suppliers = this.lookupService.getAllSuppliers();
        return suppliers;
    }

    public Integer getInsurerId() {

        if (!currentUser.getIsCHOXAdmin()) {
            insurerId = currentUser.getUser().getInsurer().getId();
        }
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        this.insurerId = insurerId;
    }

    public Integer getSupplierId() {
        if (!currentUser.getIsCHOXAdmin()) {
            supplierId = currentUser.getUser().getChorganisation().getId();
        }
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ACTION : UPDATE USER PASSWORD">
    public String updateUserPassword() throws Exception {

        try {
            
            ActionResponse response = adminUserService.updateUserPassword(model);
            setActionResponse(response);
            
        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }
        return SUCCESS;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ACTION : GRID VIEW - UPDATE STATUS & PASSWORD">
    public String triggerStatus() throws Exception {

        try {

            ActionResponse response = adminUserService.triggerUserStatus(model);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String triggerPasswordExpiredStatus() {

        try {

            ActionResponse response = adminUserService.triggerPasswordExpiredStatus(model);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }
    // </editor-fold>
}
