/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Incident;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.Date;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class IncidentAction extends BaseModelAction implements ModelDriven<Incident>, Preparable {

    private Incident model;

    public Incident getModel() {
        return model;
    }

    public void prepare() throws Exception {
        model = getClaim().getIncident();
        if (model == null) {
            model = new Incident();
        }
    }

    public String updateModel() {
        try {
            boolean isTransient = model.isTransient();
            Claim claim = getClaim();

            claim.setIncident(model);

            this.claimService.updateClaim(claim);
            if (isTransient) {
                this.getActionResponse().AssignNewIdResult(model.getId());
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
