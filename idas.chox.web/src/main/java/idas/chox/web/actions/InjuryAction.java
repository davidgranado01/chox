package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.service.security.TabAccessibility;


/**
 *
 * @author Emmanuel
 */
public class InjuryAction extends ClaimModelAction<Injury> {
    private static final Logger LOG = LoggerFactory.getLogger(InjuryAction.class);
    
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

    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("InjuryAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("InjuryAction validate success");
        }
        LOG.debug(" InjuryAction validation is not done as claim is null");
    }
}
