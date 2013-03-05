package idas.chox.web.actions;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;
import idas.chox.service.security.TabAccessibility;

public class WitnessAction extends ClaimModelAction<Witness> {

    @Override
    public Witness loadModel() {

        Incident incident = claim.getIncident();

        if (incident != null) {
            Witness witness = incident.getWitness();
            if (witness != null) {
                return witness;
            }
        }

        return new Witness();
    }

    @Override
    public String updateModel() {
        Incident incident = claim.getIncident();
        if (incident == null) {
            incident = new Incident();
        }

        incident.setWitness(model);
        model.setIncident(incident);
        claim.setIncident(incident);

        return super.updateModel();
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }
}
