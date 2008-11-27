/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Incident;
import chox.services.IncidentService;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class IncidentAction extends BaseAction implements ModelDriven<Incident>, Preparable {

    private IncidentService service;
    private Incident model;
    private int id = -1;
    private String actionResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIncidentService(IncidentService service) {
        this.service = service;
    }

    public Incident getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (id == -1) {
            model = new Incident();
        } else {
            model = service.getIncident(id);
        }
    }

    public String getActionResult() {
        return actionResult;
    }

    public String updateIncident() {
        try {
            this.service.updateIncident(model);
            this.actionResult = "1";
        } catch (Exception ex) {
            this.actionResult = "0";
        }
        return SUCCESS;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }
}
