package chox.web.actions;

import chox.model.ChoBand;
import chox.services.ChoBandService;
import chox.services.ChoBandOrganisationService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doInsurerChoBandAction extends BaseAction implements ModelDriven<ChoBand>, Preparable {

    private ChoBandService service;
    private String objectId;
    private ChoBand model;
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
    
    public ChoBand getModel() {
        return model;
    }

    public void setModel(ChoBand model) {
        this.model = model;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    
    public void setChoBandService(ChoBandService service)
    {
        this.service = service;
    }


    public String deleteChoBand() throws Exception{

        try {

            System.out.println("START DELETING PROCESS");

            ChoBand thisObject = this.service.getObject(Integer.valueOf(objectId));

            if(service.isChoBandOccupied(thisObject)){
                System.out.println("START DELETING PROCESS - CANNOT DELETE");
                actionResult = "Please remove the credit hire organisation from this CHO band before you delete!";
                return SUCCESS;
            }else{
                System.out.println("START DELETING PROCESS - ALLOW TO DELETE");
                this.service.deleteObject(thisObject);
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
        
        //TODO: CHECK INSURER NAME
        
        try {
            
            actionResult = "Your changes have been saved.";   
            /*
            if(this.isNew){
                
                if(this.service.isInsurerNameExist(model.getName())){
                    actionResult = "Insurer name already exist!"; 
                    return SUCCESS;
                }
                
            }
            */
            
          // model = this.service.updateObject(model);
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
            model = new ChoBand();
            this.isNew = true;
        } else {
            model = service.getObject(Integer.valueOf(objectId));
            this.isNew = false;
        }
    }
}
