package chox.web.actions;

import chox.model.Insurer;
import chox.services.InsurerService;

public class doInsurerAction extends BaseAction {

    private InsurerService service;
    private String objectId;
    
    public void setInsurerService(InsurerService service)
    {
        this.service = service;
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

}
