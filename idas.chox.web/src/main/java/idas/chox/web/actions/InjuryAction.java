package idas.chox.web.actions;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.service.security.ApplicationAccessibility;
import org.springframework.security.AccessDeniedException;

/**
 *
 * @author Emmanuel
 */
public class InjuryAction extends ClaimModelAction<Injury> {

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
        if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
            throw new AccessDeniedException("Attempt to access a claim that you do not own.");
        }

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
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }
}
