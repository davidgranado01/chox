package chox.web.actions;

import chox.model.BreBand;
import chox.services.BreBandService;
import chox.services.InsurerService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doInsurerBreBandAction extends BaseAction implements ModelDriven<BreBand>, Preparable {

    private BreBandService service;
    private InsurerService insurerService;
    private String objectId;
    private int insurerId = -1;
    private BreBand model;
    private String actionResult;    
    private boolean isNew;
    
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    
    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
    
    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
    
    public BreBand getModel() {
        return model;
    }

    public void setModel(BreBand model) {
        this.model = model;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public void setBreBandService(BreBandService service)
    {
        this.service = service;
    }

    public void setInsurerService(InsurerService insurerService){
        this.insurerService = insurerService;
    }

    public String deleteBreBand() throws Exception{

        try {
            
            
            
            
//             <<BRE NAME>> 
            if(service.isBreBandOccupied(model)){
            
                actionResult = "F: You cannot delete '"+model.getName()+"' because it is currently being used by one or more Credit Hire Organisations. Please remove the Credit Hire Organisations from this BRE and try again";
                return SUCCESS;
            }else{
                this.service.deleteObject(model);
                actionResult = "D:'"+model.getName()+"' has deleted";
                
            }
            
            
            
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
            
            if(this.isNew){    
                model.setInsurer(insurerService.getObject(insurerId));
            }
           
            if(service.isBreBandNameExist(model)){
                actionResult = "Selected Band Name already exists"; 
                return SUCCESS;
            }

            actionResult = "";
            
            this.service.updateObject(model);
            
            if(this.isNew){
                actionResult = "objectId:"+model.getId();
                this.isNew = false;
            }
            
        } catch (Exception ex) {
            throw ex; 
        }
        
        return SUCCESS;
    }
    
    public void prepare() throws Exception {
        if (Integer.valueOf(objectId) <= 0) {
            model = new BreBand();
            this.isNew = true;
        } else {
            model = service.getObject(Integer.valueOf(objectId));
            this.isNew = false;
        }
    }
}
