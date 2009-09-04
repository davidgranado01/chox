package chox.web.actions;

import chox.model.Insurer;
import chox.model.Workgroup;
import chox.services.InsurerService;
import chox.services.WorkgroupService;
import chox.web.viewdata.ActionResponse;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doInsurerWorkgroupAction extends BaseAction implements ModelDriven<Workgroup>, Preparable {

    protected int insurerId = -1;
    protected int workgroupId = -1;
    protected String workgroupName;
    private Workgroup model;
    private String actionResult;
    protected WorkgroupService service;
    protected InsurerService insurerService;

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public Workgroup getModel() {
        return model;
    }

    public void setModel(Workgroup model) {
        this.model = model;
    }


    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.service = workgroupService;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }
        
    public String doRenderActionPage(){
        return SUCCESS;
    }
    
    public String addObject(){
        
        String ackMsg = "";
        
        if(!service.isWorkgroupExist(insurerId, workgroupName)){
            
            model.setInsurer(insurerService.getObject(insurerId));
            model.setName(workgroupName);
            model.setStatus(true);
            service.updateObject(model);
            
            ackMsg = "Workgroup '" + workgroupName + "' has been created";
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);
            
        }else{
            
            ackMsg = "Workgroup '" + workgroupName + "' already exists";
            getActionResponse().AddError(ackMsg);
            
        }
        
        return SUCCESS;
    }    

    public String triggerObject(){
        
        boolean isAllowedToChange = true;
        String ackMsg = "";
        Insurer insurer = insurerService.getObject(insurerId);
        
        // CHECK WORKGROUPS STATUS IF CHANGE FROM ACTIVE TO INACTIVE
        // WORKGROUP FEATUERE IS ENABLE
        // EXCEPT THE WORKGROUP ITSELF, DO NOT HAVE ANY ACTIVE WORKGROUP
        if(insurer.isWorkgroupEnable() && model.isStatus() && !service.isWorkgroupAllowToInactive(insurerId, model.getId())){
            isAllowedToChange = false;
        }
        
        if(isAllowedToChange){
            model.setStatus(!model.isStatus());
            service.updateObject(model);
        }else{
            ackMsg = "Unable to de-activate this workgroup. Must maintain at least one active workgroup for this insurer.";
            getActionResponse().AddError(ackMsg);            
        }
        
        return SUCCESS;
        
    }
    
    public String removeObject(){
        
        Insurer insurer = insurerService.getObject(insurerId);
        
        String ackMsg = "";
        
        // WORKGROUP FEATUERE IS ENABLE
        // EXCEPT THE WORKGROUP ITSELF, DO NOT HAVE ANY ACTIVE WORKGROUP        
        if(insurer.isWorkgroupEnable() && !service.isWorkgroupAllowToInactive(insurerId, model.getId())){
            ackMsg = "Unable to remove this workgroup. Must maintain at least one active workgroup for this insurer.";
            getActionResponse().AddError(ackMsg);                   
            return SUCCESS;
        }        
        
        if(service.isWorkgroupDeletable(model.getId())){
            
            service.DeleteObject(model);     
            ackMsg = "Workgroup '"+model.getName()+"' has been deleted";  
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, ackMsg);    
            
        }
        else
        {
            ackMsg = "Workgroup '" + model.getName() + "' cannot be removed";
            getActionResponse().AddError(ackMsg);
        }
        
        return SUCCESS;
    }

    public void prepare() throws Exception {
        
        if (Integer.valueOf(workgroupId) <= 0) {
            model = new Workgroup();
        } else {
            model = service.getObject(workgroupId);
        }
        
    }    
}
