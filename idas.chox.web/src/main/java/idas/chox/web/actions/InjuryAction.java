/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.services.IncidentService;
import idas.chox.core.services.InjuryService;
import idas.chox.web.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class InjuryAction extends BaseModelAction implements ModelDriven<Injury>, Preparable {

    private InjuryService service;
    private Injury model;
    private int incidentId;
    private IncidentService incidentService;

    public void setInjuryService(InjuryService service) {
        this.service = service;
    }

    public Injury getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Injury();
        } else {
            model = service.getObject(objectId);
        }
    }

    public String updateModel() {
        try {
            if (objectId <= 0) {
                Incident incident = incidentService.getObject(incidentId);
                this.model.setIncident(incident);
                this.service.updateObject(model);
                this.actionResult = "new:" + model.getId();
            } else {
                this.service.updateObject(model);
                this.actionResult = "";
            }
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public void setIncidentService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }
}
