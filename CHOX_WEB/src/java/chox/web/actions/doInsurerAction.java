package chox.web.actions;

import chox.model.Insurer;
import chox.services.BreBandService;
import chox.services.InsurerAliasService;
import chox.services.InsurerService;
import chox.services.WorkgroupService;
import chox.web.viewdata.ActionResponse;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doInsurerAction extends BaseAction implements ModelDriven<Insurer>, Preparable {

    private String objectId;
    private Insurer model;
    private String actionResult;    
    private boolean isNew;
   
    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
    
    public Insurer getModel() {
        return model;
    }

    public void setModel(Insurer model) {
        this.model = model;
    }
    
    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
    
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public String triggerStatus() throws Exception{

        Insurer thisObject = this.service.getObject(Integer.valueOf(objectId));

        if(thisObject.isStatus()){
            thisObject.setStatus(false);
        }else{
            thisObject.setStatus(true);
        }
        
        try {
            this.service.updateObject(thisObject);
            
        } catch (Exception ex) {
            throw ex; 
        }
        
        return SUCCESS;
    }

    public String doRenderActionPage(){
        return SUCCESS;
    }  
    
    public String triggerInsurerWorkgroupFeature() throws Exception {

        Insurer thisObject = this.service.getObject(Integer.valueOf(objectId));

        // ORIGINAL IS FALSE, CHANGE TO TRUE
        if(!thisObject.isWorkgroupEnable() && !workgroupService.isInsurerAllowToEnableWorkgroup(thisObject)){
            this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Please make sure there is atleast one active workgroup exist in order to enable workgroup function");
            return SUCCESS;
            
        }
        
        thisObject.setWorkgroupEnable(!thisObject.isWorkgroupEnable());
        this.service.updateObject(thisObject);
        return SUCCESS;
    }
    
    public String updateModel() throws Exception {

        try {
                      
            if(this.isNew){
                
                if(this.service.isInsurerNameExist(model.getName())){
                    this.getActionResponse().AddError("Insurer name already exist!");
                    return SUCCESS;
                }
                
            }else{
                
                if(model.isWorkgroupEnable() && !workgroupService.isInsurerAllowToEnableWorkgroup(model)){
                    this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Please make sure there is atleast one active workgroup exist in order to enable workgroup function");
                    return SUCCESS;
                }
                
            }

            model = this.service.updateObject(model);            
            
            if(this.isNew){
                
                if(model.isWorkgroupEnable()){
                    workgroupService.createDefaultRecord(model);
                }
                
                insurerAliasService.createDefaultRecord(model);
                breBandService.createDefaultRecord(model);
                this.getActionResponse().AssignNewIdResult(model.getId());
                this.isNew = false;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }
        
        return SUCCESS;
    }

    public void prepare() throws Exception {
        if (Integer.valueOf(objectId) <= 0) {
            model = new Insurer();
            this.isNew = true;
        } else {
            model = service.getObject(Integer.valueOf(objectId));
            this.isNew = false;
        }
    }
    
    private InsurerAliasService insurerAliasService;
    private WorkgroupService workgroupService;
    private BreBandService breBandService;
    private InsurerService service;
    
    public void setInsurerService(InsurerService service) { this.service = service; }
    public void setBreBandService(BreBandService breBandService) { this.breBandService = breBandService; }
    public void setWorkgroupService(WorkgroupService workgroupService) { this.workgroupService = workgroupService; }
    public void setInsurerAliasService(InsurerAliasService insurerAliasService) { this.insurerAliasService = insurerAliasService; }
    
    
}
