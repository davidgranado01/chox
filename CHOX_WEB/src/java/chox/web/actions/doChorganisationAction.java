package chox.web.actions;

import chox.model.Chorganisation;
import chox.services.ChorganisationService;

public class doChorganisationAction extends BaseAction {

    private ChorganisationService service;
    private String objectId;
    
    public void setChorganisationService(ChorganisationService service)
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
        
        System.out.println("doChorganisationAction>triggerStatus");
        
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

}
