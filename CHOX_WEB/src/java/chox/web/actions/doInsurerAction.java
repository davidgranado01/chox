package chox.web.actions;

import chox.model.Insurer;
import chox.services.InsurerService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doInsurerAction extends BaseAction implements ModelDriven<Insurer>, Preparable {

    private InsurerService service;
    private String objectId;
    private Insurer model;
    private String actionResult;

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
    
    public Insurer getModel() {
        return model;
    }

    public void setModel(Insurer model) {
        this.model = model;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public void setInsurerService(InsurerService service)
    {
        this.service = service;
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
    
    public String updateModel() throws Exception {
        
        try {
            this.service.updateObject(model);
            actionResult = "Your changes have been saved.";
        } catch (Exception ex) {
            throw ex; 
        }
        
        return SUCCESS;
    }

    public void prepare() throws Exception {
        if (Integer.valueOf(objectId) <= 0) {
            model = new Insurer();
        } else {
            model = service.getObject(Integer.valueOf(objectId));
        }
    }
}
