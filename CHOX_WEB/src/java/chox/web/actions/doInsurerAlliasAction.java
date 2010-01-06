/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.InsurerAllias;
import chox.services.InsurerAlliasService;
import chox.services.InsurerService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doInsurerAlliasAction extends BaseAction implements ModelDriven<InsurerAllias>, Preparable {
    
    protected int insurerId=-1;
    protected int insurerAlliasId=-1;
    protected String insurerAlliasName;
    private InsurerAllias model;
    private String actionResult;
    protected InsurerAlliasService service;
    protected InsurerService insurerService;
    
    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
    
    public void setInsurerService(InsurerService insurerService)
    {
        this.insurerService = insurerService;
    }

    public void setInsurerAlliasService(InsurerAlliasService service)
    {
        this.service = service;
    }
    
    public int getInsurerAlliasId() {
        return insurerAlliasId;
    }

    public void setInsurerAlliasId(int insurerAlliasId) {
        this.insurerAlliasId = insurerAlliasId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getInsurerAlliasName() {
        return insurerAlliasName;
    }

    public void setInsurerAlliasName(String insurerAlliasName) {
        this.insurerAlliasName = insurerAlliasName;
    }
    
    public String removeObject(){
        service.DeleteObject(model);
        return SUCCESS;
    }
    
    public String addObject(){
        
        if(!service.isInsurerAlliasExist(insurerId, insurerAlliasName)){
            model = new InsurerAllias();
            model.setAlliasName(insurerAlliasName);
            model.setInsurer(insurerService.getObject(insurerId));
            service.updateObject(model);
        }else{
            actionResult = "Alias '"+insurerAlliasName+"' already exists";
        }
        
        return SUCCESS;
    }

    public InsurerAllias getModel() {
        return this.model;
    }

    public void prepare() throws Exception {
        
        if (Integer.valueOf(insurerAlliasId) <= 0) {
            model = new InsurerAllias();
        } else {
            model = service.getObject(insurerAlliasId);
        }
    }
    
}
