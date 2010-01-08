/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Solicitor;
import idas.chox.service.security.ApplicationAccessibility;
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

    public Solicitor getModel() {
        return model;
    }

    public void prepare() throws Exception {
        Claim claim = getClaim();

        if (claim != null) {
            Incident incident = claim.getIncident();

            if (incident == null) {
                incident = new Incident();
                claim.setIncident(incident);
            }

            Injury injury = incident.getInjury();

            if (injury == null) {
                injury = new Injury();
                injury.setIncident(incident);
                incident.setInjury(injury);
            }

            model = injury.getSolicitor();

            if (model == null) {
                model = new Solicitor();
                injury.setSolicitor(model);
            }
        }
    }

    public String updateModel() {
        try {           
            Claim claim = getClaim();
            this.claimService.updateClaim(claim);           
        } catch (Exception ex) {
            logger.error(ex);
            getActionResponse().AddError(ex.getMessage());
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
