package chox.web.actions;

import chox.model.WebUser;
import chox.services.UserService;

public class doUserAction extends BaseAction {

    private UserService service;
    private String objectId;
    
    public void setUserService(UserService service)
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
                
        WebUser thisUser = null;
        thisUser = this.service.getObject(Integer.valueOf(objectId));
        
        if(thisUser.getStatus()==1){
            thisUser.setStatus(0);
        }else{
            thisUser.setStatus(1);
        }
        
        try {
            
            this.service.updateObject(thisUser);
        } catch (Exception ex) {
            throw ex; 
        }
        
        return SUCCESS;
    }

}
