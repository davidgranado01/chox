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

    private UserService service;
    private String objectId;
    private WebUser model;
    private String orgTypeId;
    private String mode;
    private List insurers;
    private List suppliers;
    private LookupService lookupService;
    private InsurerService insurerService;
    private ChorganisationService chorganisationService;
    private WebUserUserRoleService webUserUserRoleService;
    private ClaimService claimService;
    private Integer insurerId = -1;
    private Integer supplierId = -1;
    private PermissionedUser currentUser = getAuthenticatedUser();
    private boolean isOrgSelectable = false;
    private String tabIndex;

    ;

    public String getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean isIsOrgSelectable() {
        if (currentUser.getIsCHOXAdmin()) {
            isOrgSelectable = true;
        }
        return isOrgSelectable;
    }

    public boolean isWorkgroupEnabled() {

        boolean isEnable = false;

        if (model.getInsurer() != null) {
            Insurer insurer = insurerService.getObject(model.getInsurer().getId());
            isEnable = insurer.isWorkgroupEnable();
        }
        return isEnable;
    }

    public List getInsurers() {
        insurers = this.lookupService.getInsurers();
        return insurers;
    }

    public List getSuppliers() {
        suppliers = this.lookupService.getAllSuppliers();
        return suppliers;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public String getOrgTypeId() {
        return orgTypeId;
    }

    public void setOrgTypeId(String orgTypeId) {
        this.orgTypeId = orgTypeId;
    }

    public String getOrgTypeName() {

        String orgTypeName = "N/A";

        if (this.orgTypeId.equalsIgnoreCase("1")) {
            orgTypeName = "Sherwood Organisation Users";
        } else if (this.orgTypeId.equalsIgnoreCase("2")) {
            orgTypeName = "Insurer Organisation Users";
        } else if (this.orgTypeId.equalsIgnoreCase("3")) {
            orgTypeName = "Credit Hire Organisation Users";
        }

        return orgTypeName;
    }

    public String getOrgName() {

        String sOutput = "N/A";

        if (this.orgTypeId.equalsIgnoreCase("1")) {
            sOutput = "Sherwood";
        } else {
            sOutput = model.getOrganisationName();
        }

        return sOutput;
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

    public void setUserService(UserService service) {
        this.service = service;
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
                this.service.updateObject(thisObject);
            }

        } catch (Exception ex) {
            throw ex;
        }

        return SUCCESS;
    }

    public String triggerPasswordExpiredStatus() {
        WebUser thisObject = model;
        thisObject.setIsExpired(!thisObject.getIsExpired());
        this.service.updateObject(thisObject);
        return SUCCESS;

    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String updateModel() throws Exception {

        try {

            model.setLastModifiedBy(this.getAuthenticatedUser().getUser());
            model.setLastModifiedDate(DateHelper.getCurrentTimeStamp());

            if (mode.equalsIgnoreCase("New")) {

                doAddNewObject();

            } else {

                if (!this.service.isEmailExist(model.getEmail(), model.getId())) {
                    this.service.updateObject(model);
                    this.getActionResponse().AssignMessageResult("Your changes have been saved.");
                } else {
                    this.getActionResponse().AddError("Email Address already exist!");
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }

    private void encodePassword() {
        PasswordEncoder passwordEncoder = new Md5PasswordEncoder();
        model.setPassword(passwordEncoder.encodePassword(model.getPassword(), null));
    }

    public String updateUserPassword() throws Exception {

        try {
            model.setLastModifiedDate(DateHelper.getCurrentTimeStamp());
            model.setLastModifiedBy(this.getAuthenticatedUser().getUser());
            encodePassword();
            this.service.updateObject(model);
            this.getActionResponse().AssignMessageResult("Your changes have been saved.");
        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }
        return SUCCESS;
    }

    private boolean doAddNewObject() {

        boolean bFlag = false;

        if (!this.service.isEmailExist(model.getEmail())) {

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

            if (this.service.updateObject(model)) {

                if (webUserUserRoleService.addBaseNewUserRole(model.getId(), Integer.valueOf(this.orgTypeId))) {
                    bFlag = true;
                    this.getActionResponse().AssignNewIdResult(model.getId());
                }

            } else {
                // actionResult = "Please try again!";
                this.getActionResponse().AddError("Please try again!");
            }

        } else {
            // actionResult = "Email Address already exist!";
            this.getActionResponse().AddError("Email Address already exist!");
        }

        return bFlag;
    }

    public void prepare() throws Exception {

        if (Integer.valueOf(objectId) <= 0) {
            model = new WebUser();
            model.setClaimHandler(false);
            mode = "New";
        } else {
            model = service.getUsers(Integer.valueOf(objectId));
            mode = "Edit";

        }
    }
}
