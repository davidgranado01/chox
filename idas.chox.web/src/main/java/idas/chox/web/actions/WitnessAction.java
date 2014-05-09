package idas.chox.web.actions;


import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;
import idas.chox.service.security.TabAccessibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WitnessAction extends ClaimModelAction<Witness> {

    private static final Logger LOG = LoggerFactory.getLogger(WitnessAction.class);
    
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
    
    @Override
    public void validate() {

        if (claim != null && (claim.getChorganisation() != null || claim.getInsurer() != null)) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("Validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("WitnessAction validated");
        } else {
            LOG.info(" WitnessAction validation not done as claim or cho or insurer is null");
        }
    }
}
