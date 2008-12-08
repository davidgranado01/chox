/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Incident;
import chox.model.Injury;
import chox.model.Solicitor;
import chox.services.IncidentService;
import chox.services.InjuryService;
import chox.services.SolicitorService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class SolicitorAction extends BaseModelAction implements ModelDriven<Solicitor>, Preparable {

    private SolicitorService service;
    private Solicitor model;
    private InjuryService injuryService;
    private IncidentService incidentService;
    private int injuryId;
    private int incidentId;

    public void setSolicitorService(SolicitorService service) {
        this.service = service;
    }

    public Solicitor getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Solicitor();
        } else {
            model = service.getObject(objectId);
        }
    }

    public String updateModel() {
        try {
            if (objectId <= 0) {
                Injury injury = this.injuryService.getObject(injuryId);
                if (injury == null) {
                    Incident incident = this.incidentService.getObject(this.getIncidentId());
                    injury = new Injury();
                    injury.setIncident(incident);    
                    this.injuryService.updateObject(injury);
                }
                this.model.setInjury(injury);
            }

            this.service.updateObject(model);
            this.actionResult = "";
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

    public void setInjuryService(InjuryService injuryService) {
        this.injuryService = injuryService;
    }

    public int getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(int injuryId) {
        this.injuryId = injuryId;
    }

    public void setIncidentService(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }
}
