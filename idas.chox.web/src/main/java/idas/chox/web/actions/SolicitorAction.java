package idas.chox.web.actions;

import idas.chox.core.model.Incident;
import idas.chox.service.security.TabAccessibility;
import idas.chox.core.model.Injury;

/**
 *
 * @author Emmanuel
 */
public class SolicitorAction extends ClaimModelAction<Injury> {

    @Override
    public Injury loadModel() {
        Incident incident = claim.getIncident();
        if (incident != null) {
            Injury injury = incident.getInjury();
            if (injury != null) {
                return injury;
            }
        }
        return new Injury();
    }

    @Override
    public String updateModel() {
        Incident incident = claim.getIncident();
        if (incident == null) {
            incident = new Incident();
        }
        incident.setInjury(model);
        model.setIncident(incident);
        claim.setIncident(incident);

        return super.updateModel();
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }
}
