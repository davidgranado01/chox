/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Solicitor;
import idas.chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Injury;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class SolicitorAction extends BaseModelAction implements ModelDriven<Solicitor>, Preparable {

    private Solicitor model;
    private int incidentId;

    public Solicitor getModel() {
        return model;
    }

    public void prepare() throws Exception {
        Claim claim = getClaim();

        if (claim != null && claim.getIncident() != null && claim.getIncident().getInjury() != null) {
            model = claim.getIncident().getInjury().getSolicitor();
        } else {
            model = new Solicitor();
        }
    }

    public String updateModel() {
        try {
            boolean isTransient = model.isTransient();
            Claim claim = getClaim();
            Incident incident = claim.getIncident();

            if (incident == null) {
                incident = new Incident();
                claim.setIncident(incident);
            }

            Injury injury = incident.getInjury();
            if (injury == null) {
                injury = new Injury();
                incident.setInjury(injury);
            }

            injury.setSolicitor(model);

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
}
