/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.web.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class InjuryAction extends BaseModelAction implements ModelDriven<Injury>, Preparable {

    private Injury model;
    private int incidentId;

    public Injury getModel() {
        return model;
    }

    public void prepare() throws Exception {
        Claim claim = getClaim();

        if (claim != null && claim.getIncident() != null) {
            model = claim.getIncident().getInjury();
        } else {
            model = new Injury();
        }
    }

    public String updateModel() {
        try {
            boolean isTransient = model.isTransient();
            Claim claim = getClaim();
            Incident incident = claim.getIncident();

            if (incident == null) {
                incident = new Incident();
            }

            incident.setInjury(model);

            claim.setIncident(incident);

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

    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

}
