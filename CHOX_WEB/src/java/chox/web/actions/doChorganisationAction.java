package chox.web.actions;

import chox.model.Chorganisation;
import chox.services.ChorganisationService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doChorganisationAction extends BaseAction implements ModelDriven<Chorganisation>, Preparable {

    private ChorganisationService service;
    private String objectId;
    private Chorganisation model;
    private String actionResult;
    private boolean isNew;

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
    
    
    public Chorganisation getModel() {
        return model;
    }

    public void setModel(Chorganisation model) {
        this.model = model;
    }
    
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public void setChorganisationService(ChorganisationService service)
    {
        this.service = service;
    }

    public String triggerStatus() throws Exception{
        
        Chorganisation thisObject = null;
        thisObject = this.service.getObject(Integer.valueOf(objectId));
        
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
    
    public String updateModel() throws Exception {
        
        try {
            if(this.isNew){
                
                if(this.service.isChorgNameExist(model.getName())){
                    actionResult = "Insurer name already exist!"; 
                    return SUCCESS;
                }
            }
            
            model = this.service.updateObject(model);
            this.isNew = false;
            actionResult = "Your changes have been saved.";
            
        } catch (Exception ex) {
            throw ex; 
        }
        
        return SUCCESS;
    }

    public void prepare() throws Exception {
        if (Integer.valueOf(objectId) <= 0) {
            model = new Chorganisation();
            this.isNew = true;
        } else {
            model = service.getObject(Integer.valueOf(objectId));
            this.isNew = false;
        }
    }

}
