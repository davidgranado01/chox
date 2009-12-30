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
import idas.chox.web.viewdata.ActionResponse;
import java.util.List;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.providers.encoding.PasswordEncoder;

public class doUserAction extends BaseAction implements ModelDriven<WebUser>, Preparable {

    private UserService userService;
    private String objectId;
    private WebUser model;
    private String organisationTypeId;
    private List insurers;
    private List suppliers;
    private LookupService lookupService;
    private InsurerService insurerService;
    private Integer insurerId = -1;
    private Integer supplierId = -1;
    private ChorganisationService chorganisationService;
    private WebUserUserRoleService webUserUserRoleService;
    private ClaimService claimService;
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
            Insurer insurer = insurerService.getObject(model.getInsurer().getId());
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

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService) {
        this.webUserUserRoleService = webUserUserRoleService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ACTION : ADD OR EDIT USER">

    public String updateModel() throws Exception {

        try {

            model.setLastModifiedBy(this.getAuthenticatedUser().getUser());
            model.setLastModifiedDate(DateHelper.getCurrentTimeStamp());

            if (getIsNew()) {

                doAddNewObject();

            } else {

                if (!this.userService.isUserNameExist(model.getUserName(), model.getId())) {
                    this.userService.updateObject(model);
                } else {
                    this.getActionResponse().AddError("User Name is already exist!");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }

    private void doAddNewObject() {

        if (!this.userService.isUserNameExist(model.getUserName())) {

            model.setCreatedBy(this.getAuthenticatedUser().getUser());
            model.setCreatedDate(DateHelper.getCurrentTimeStamp());
            model.setIsExpired(true);

            if (insurerId > 0) {
                Insurer selectInsurer = insurerService.getObject(insurerId);
                model.setInsurer(selectInsurer);
            }

            if (supplierId > 0) {
                Chorganisation selectChorganisation = chorganisationService.getObject(supplierId);
                model.setChorganisation(selectChorganisation);
            }

            encodePassword();

            if (this.userService.updateObject(model)) {

                if (webUserUserRoleService.addBaseNewUserRole(model.getId(), Integer.valueOf(this.organisationTypeId))) {
                    this.getActionResponse().AssignNewIdResult(model.getId());
                }

            } else {
                this.getActionResponse().AddError("Please try again!");
            }

        } else {
            this.getActionResponse().AddError("User Name is already exist!!");
        }

    }

    private void encodePassword() {
        PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        model.setPassword(passwordEncoder.encodePassword(model.getPassword(), null));
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
            model.setLastModifiedDate(DateHelper.getCurrentTimeStamp());
            model.setLastModifiedBy(this.getAuthenticatedUser().getUser());
            encodePassword();
            this.userService.updateObject(model);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }
        return SUCCESS;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="ACTION : GRID VIEW - UPDATE STATUS & PASSWORD">
    public String triggerStatus() throws Exception {

        try {

            WebUser thisObject = model;
            thisObject.setStatus(!thisObject.getStatus());

            boolean isAllowUpdate = true;

            if (!thisObject.getStatus() && claimService.isUserHasOpenClaim(thisObject.getId())) {
                String ackMsg = "This user currently has assigned claims. Please reassign these claims before de-activating this user account";
                getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);
                isAllowUpdate = false;
            }

            if (isAllowUpdate) {
                thisObject.setLastModifiedBy(this.getAuthenticatedUser().getUser());
                thisObject.setLastModifiedDate(DateHelper.getCurrentTimeStamp());
                this.userService.updateObject(thisObject);
            }

        } catch (Exception ex) {
            throw ex;
        }

        return SUCCESS;
    }

    public String triggerPasswordExpiredStatus() {
        WebUser thisObject = model;
        thisObject.setIsExpired(!thisObject.getIsExpired());
        this.userService.updateObject(thisObject);
        return SUCCESS;

    }
    // </editor-fold>
}
