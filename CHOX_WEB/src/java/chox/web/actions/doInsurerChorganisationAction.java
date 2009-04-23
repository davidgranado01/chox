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

public class doInsurerChorganisationAction extends BaseAction implements ModelDriven<InsurerChorganisation>, Preparable {

    private InsurerChorganisation model;
    protected int objectId=-1;
    protected int insurerId=-1;
    protected int chorganisationId=-1;
    private String actionResult;
    protected InsurerChorganisationService service;
    protected ChorganisationService chorganisationService;
    protected InsurerService insurerService;

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
        service.triggerStatus(model);
        return SUCCESS;
    }
    
    public String addObject(){
        
        if(!service.isInactiveInsurerChorganisationExist(insurerId, chorganisationId)){
            
            System.out.println("INACTIVE NOT EXISTS");
            model.setChorganisation(chorganisationService.getObject(chorganisationId));
            model.setInsurer(insurerService.getObject(insurerId));
            model.setStatus(true);
            service.updateObject(model);
            
        }else{
            
            System.out.println("INACTIVE EXISTS");
            model = service.getInsurerChorganisationObject(insurerId, chorganisationId);
            service.triggerStatus(model);
            
        }
        
        return SUCCESS;
    }
    

}
