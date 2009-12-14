/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Incident;
import idas.chox.core.services.IncidentService;
import idas.chox.core.util.DateHelper;
import idas.chox.web.security.ApplicationAccessibility;
import java.util.Date;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class IncidentAction extends BaseModelAction implements ModelDriven<Incident>, Preparable {

    private IncidentService service;
    private Incident model;

    public void setIncidentService(IncidentService service) {
        this.service = service;
    }

    public Incident getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Incident();
        } else {
            model = service.getObject(objectId);
        }
    }

    public String updateModel() {
        try {
            if (objectId <= 0) {
                Claim c = claimService.getClaim(getClaimId());
                c.setIncident(model);
                this.claimService.updateClaim(c);
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

    public String getTime() {
        return DateHelper.TimeFormat.format(model.getDate());
    }

    public void setTime(String time) {
        if (model != null) {
            try {
                Date a = model.getDate();
                Date b = DateHelper.TimeFormat.parse(time);
                model.setDate(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
