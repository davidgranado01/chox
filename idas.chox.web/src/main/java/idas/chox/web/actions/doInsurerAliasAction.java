/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerService;

public class doInsurerAliasAction extends BaseAction implements ModelDriven<InsurerAlias>, Preparable {

    protected int insurerId = -1;
    protected int insurerAliasId = -1;
    protected String insurerAliasName;
    private InsurerAlias model;
    private String actionResult;
    protected InsurerAliasService service;
    protected InsurerService insurerService;

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setInsurerAliasService(InsurerAliasService service) {
        this.service = service;
    }

    public int getInsurerAliasId() {
        return insurerAliasId;
    }

    public void setInsurerAliasId(int insurerAliasId) {
        this.insurerAliasId = insurerAliasId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getInsurerAliasName() {
        return insurerAliasName;
    }

    public void setInsurerAliasName(String insurerAliasName) {
        this.insurerAliasName = insurerAliasName;
    }

    public String removeObject() {
        service.DeleteObject(model);
        return SUCCESS;
    }

    public String addObject() {

        if (!service.isInsurerAliasExist(insurerId, insurerAliasName)) {
            model = new InsurerAlias();
            model.setAliasName(insurerAliasName);
            model.setInsurer(insurerService.getObject(insurerId));
            service.updateObject(model);
        } else {
            actionResult = "Alias '" + insurerAliasName + "' already exists";
        }

        return SUCCESS;
    }

    public InsurerAlias getModel() {
        return this.model;
    }

    public void prepare() throws Exception {

        if (Integer.valueOf(insurerAliasId) <= 0) {
            model = new InsurerAlias();
        } else {
            model = service.getObject(insurerAliasId);
        }
    }
}
