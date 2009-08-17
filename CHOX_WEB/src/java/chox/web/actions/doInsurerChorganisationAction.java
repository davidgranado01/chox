/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.InsurerChorganisation;
import chox.services.ChorganisationService;
import chox.services.InsurerChorganisationService;
import chox.services.InsurerService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doInsurerChorganisationAction extends AdminBaseModelAction implements ModelDriven<InsurerChorganisation>, Preparable {

    private InsurerChorganisation model;
    private int objectId=-1;
    private int insurerId=-1;
    private int chorganisationId=-1;
    private String actionResult;
    private InsurerChorganisationService service;
    private ChorganisationService chorganisationService;
    private InsurerService insurerService;

    public void setInsurerChorganisationService(InsurerChorganisationService service)
    {
        this.service = service;
    }
    
    public void setChorganisationService(ChorganisationService chorganisationService)
    {
        this.chorganisationService = chorganisationService;
    }
    
    public void setInsurerService(InsurerService insurerService)
    {
        this.insurerService = insurerService;
    }
    
    public int getChorganisationId() {
        return chorganisationId;
    }

    public void setChorganisationId(int chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }
    
    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
       
    public InsurerChorganisation getModel() {
        return this.model;
    }

    public void prepare() throws Exception {
        
        if (Integer.valueOf(objectId) <= 0) {
            model = new InsurerChorganisation();
        } else {
            model = service.getObject(objectId);
        }
    }

    public String removeObject(){
        
        String sActionMsg = "";
        boolean bActionFlag = false;
         
        try{          
            
            service.triggerStatus(model);
        
            bActionFlag = true;
            sActionMsg = getSystemLogService().getObjectActionLogMsg("DELETE", "InsurerChorganisationId:"+model.getId());
            
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM009", sActionMsg, bActionFlag, 3);         
        return SUCCESS;
    }
    
    public String addObject(){
        
        String sActionMsg = "";
        boolean bActionFlag = false;
        
        try{ 
            
            if(!service.isInactiveObjectExist(insurerId, chorganisationId)){
                model.setChorganisation(chorganisationService.getObject(chorganisationId));
                model.setInsurer(insurerService.getObject(insurerId));
                model.setStatus(true);
                service.updateObject(model);
            }else{
                model = service.getObject(insurerId, chorganisationId);
                service.triggerStatus(model);
            }
        
            bActionFlag = true;
            sActionMsg = getSystemLogService().getObjectActionLogMsg("ADD", "InsurerChorganisationId:"+model.getId());
            
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM010", sActionMsg, bActionFlag, 3); 
        
        return SUCCESS;
    }
    

}
