/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.LineOfBusiness;
import chox.services.InsurerService;
import chox.services.LineOfBusinessService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class doInsurerLineOfBusinessAction extends BaseAction implements ModelDriven<LineOfBusiness>, Preparable {
    
    protected int insurerId=-1;
    protected int lineOfBusinessId=-1;
    protected String lineOfBusinessName;
    private LineOfBusiness model;
    private String actionResult;
    protected LineOfBusinessService service;
    protected InsurerService insurerService;

    public int getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(int lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
    }

    public String getLineOfBusinessName() {
        return lineOfBusinessName;
    }

    public void setLineOfBusinessName(String lineOfBusinessName) {
        this.lineOfBusinessName = lineOfBusinessName;
    }
    
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

    public void setLineOfBusinessService(LineOfBusinessService service)
    {
        this.service = service;
    }
    
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String removeObject(){
        model.setActive(!model.isActive());
        service.updateObject(model);
        return SUCCESS;
    }
    
    public String doRenderActionPage(){
        return SUCCESS;
    }
    
    public String addObject(){

        if(!service.isLineOfBusinessExist(insurerId, lineOfBusinessName)){
            model.setInsurer(insurerService.getObject(insurerId));
            model.setName(lineOfBusinessName);
            model.setActive(true);
            service.updateObject(model);
        }else{
            actionResult = "Line of business '"+lineOfBusinessName+"' already exists";
        }
        return SUCCESS;
    }

    public LineOfBusiness getModel() {
        return this.model;
    }

    public void prepare() throws Exception {
        
        if (Integer.valueOf(lineOfBusinessId) <= 0) {
            model = new LineOfBusiness();
        } else {
            model = service.getObject(lineOfBusinessId);
        }
        
    }
    
}
