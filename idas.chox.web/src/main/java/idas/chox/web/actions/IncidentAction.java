package idas.chox.web.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Incident;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.TabAccessibility;

/**
 *
 * @author Emmanuel
 */
public class IncidentAction extends ClaimModelAction<Incident> {
    private static final Logger LOG = LoggerFactory.getLogger(IncidentAction.class);

    @Override
    public Incident loadModel() {
        Incident incident = getClaim().getIncident();
        if (incident != null) {
            return incident;
        }
        return new Incident();
    }

    @Override
    public String updateModel() {
        claim.setIncident(model);
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }

    public String getDateTime() {
        String time = model.getTime().length() > 8 ? model.getTime().substring(0, model.getTime().lastIndexOf(".")) : model.getTime();
        String dateTime = DateHelper.getEXTDateFormat().format(model.getDate()).concat(" " + time);

        return dateTime;
    }

    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("IncidentAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("IncidentAction validate success");
        }
        else {
            LOG.debug(" IncidentAction validation is not done as claim is null");
        }
    }
    
}
