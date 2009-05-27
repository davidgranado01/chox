package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.model.WebUser;
import chox.services.ChorganisationService;
import chox.services.InsurerService;
import chox.services.LineOfBusinessService;
import chox.services.LookupService;
import chox.services.UserService;
import chox.services.WebUserUserRoleService;
import chox.web.security.PermissionedUser;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.List;

public class doUserAction extends BaseAction implements ModelDriven<WebUser>, Preparable {

    private UserService service;
    private String objectId;
    private WebUser model;
    private String orgTypeId;
    private String mode;
    private List insurers;
    private List suppliers;
    private List lineOfBusinesses;
    private LookupService lookupService;
    private InsurerService insurerService;
    private ChorganisationService chorganisationService;
    private WebUserUserRoleService webUserUserRoleService;
    private LineOfBusinessService lineOfBusinessService;
    private String actionResult;
    private Integer insurerId = -1;
    private Integer supplierId = -1;
    private Integer lineOfBusinessId = -1;
    private PermissionedUser currentUser = getAuthenticatedUser();
    
    private boolean isOrgSelectable = false;

    public boolean isIsOrgSelectable() {
        if(currentUser.getIsCHOXAdmin()){
            isOrgSelectable = true;
        }
        return isOrgSelectable;
    }
    
    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
    
    public List getInsurers() {
        insurers = this.lookupService.getInsurers();
        return insurers;
    }

    public Integer getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(Integer lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
    }
    
    public List getSuppliers() {
        suppliers = this.lookupService.getAllSuppliers();
        return suppliers;
    }

    public List getLineOfBusinesses() {
        
        if(orgTypeId.equalsIgnoreCase("2")){
            lineOfBusinesses = this.lookupService.getLineOfBusinessesByInsurerId(model.getInsurer().getId());
        }
        
        return lineOfBusinesses;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getInsurerId() {
        
        if(!currentUser.getIsCHOXAdmin()){
            insurerId = currentUser.getUser().getInsurer().getId();
        }
        
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        this.insurerId = insurerId;
    }

    public Integer getSupplierId() {
        if(!currentUser.getIsCHOXAdmin()){
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
        
        if(this.orgTypeId.equalsIgnoreCase("1")){
            orgTypeName = "Sherwood Users";
        }else if(this.orgTypeId.equalsIgnoreCase("2")){
            orgTypeName = "Insurer Users";
        }else if(this.orgTypeId.equalsIgnoreCase("3")){    
            orgTypeName = "Credit Hire Users";
        }
        
        return orgTypeName;
    }
    
    public String getOrgName() {
        
        String sOutput = "N/A";
        
        if(this.orgTypeId.equalsIgnoreCase("1")){
            sOutput = "Sherwood";
        }else{
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
    
    public void setUserService(UserService service)
    {
        this.service = service;
    }

    public void setLookupService(LookupService lookupService)
    {
        this.lookupService = lookupService;
    }
    
    public void setInsurerService(InsurerService insurerService)
    {
        this.insurerService = insurerService;
    }

    public void setLineOfBusinessService(LineOfBusinessService lineOfBusinessService) {
        this.lineOfBusinessService = lineOfBusinessService;
    }
    
    public void setWebUserUserRoleService(WebUserUserRoleService webUserUserRoleService)
    {
        this.webUserUserRoleService = webUserUserRoleService;
    }
    
    public void setChorganisationService(ChorganisationService chorganisationService)
    {
        this.chorganisationService = chorganisationService;
    }
    
    public String triggerStatus() throws Exception{

        WebUser thisObject = this.service.getUsers(Integer.valueOf(objectId));
        
        if(thisObject.getStatus()){
            thisObject.setStatus(false);
        }else{
            thisObject.setStatus(true);
        }
        
        try {
            
            thisObject.setLastModifiedBy(this.getAuthenticatedUser().getUser());
            thisObject.setLastModifiedDate(DateHelper.getCurrentTimeStamp());
            this.service.updateObject(thisObject);
            
        } catch (Exception ex) {
            throw ex; 
        }
        
        return SUCCESS;
    }

    public String doRenderActionPage(){
        return SUCCESS;
    }  
    
    public String updateModel() throws Exception {
        
        try {
            
            model.setLastModifiedBy(this.getAuthenticatedUser().getUser());
            model.setLastModifiedDate(DateHelper.getCurrentTimeStamp());  
            
            if(mode.equalsIgnoreCase("New")){
                
                doAddNewObject();
                
            }else{
                
                //System.out.println("doUserAction > updateModel > lineOfBusinessId : "+lineOfBusinessId);
                
                if(lineOfBusinessId!=null && lineOfBusinessId>0){
                    model.setLineOfBusiness(lineOfBusinessService.getObject(lineOfBusinessId));
                }
                
                this.service.updateObject(model);  
                actionResult = "Your changes have been saved.";
            }
            
        } catch (Exception ex) {
            throw ex; 
        }
        
        return SUCCESS;
    }
    
    
    
    private boolean doAddNewObject(){
        
        boolean bFlag  = false;
        
        if(!this.service.isEmailExist(model.getEmail())){
                    
            model.setCreatedBy(this.getAuthenticatedUser().getUser());
            model.setCreatedDate(DateHelper.getCurrentTimeStamp());
            
            if(insurerId>0){
                Insurer selectInsurer = insurerService.getObject(insurerId);
                model.setInsurer(selectInsurer);
            }
            
            if(supplierId>0){
                Chorganisation selectChorganisation = chorganisationService.getObject(supplierId);
                model.setChorganisation(selectChorganisation);
            }
            
            if(this.service.updateObject(model)){
                
                if(webUserUserRoleService.addBaseNewUserRole(model.getId(), Integer.valueOf(this.orgTypeId))){
                    bFlag = true;
                    actionResult = "objectId:"+model.getId();
                }
                
            }else{
                actionResult = "Please try again!";
            }
            
        }else{
            actionResult = "Email Address already exist!";     
        }
        
        return bFlag;
    }

    /*
    private String getUserOrgBaseRole() {
        
        String sOutput = "-";
        
        if(this.orgTypeId.equalsIgnoreCase("1")){
            sOutput = WebUserRole.ROLE_CHOX;
        }else if(this.orgTypeId.equalsIgnoreCase("2")){
            sOutput = WebUserRole.ROLE_INS;
        }else if(this.orgTypeId.equalsIgnoreCase("3")){    
            sOutput = WebUserRole.ROLE_CHO;
        }
        
        return sOutput;
    }
    */
    
    public void prepare() throws Exception {
        
        if (Integer.valueOf(objectId) <= 0) {
            model = new WebUser();
            mode = "New";
        } else {
            model = service.getUsers(Integer.valueOf(objectId));
            mode = "Edit";
            
        }
    }    
}
