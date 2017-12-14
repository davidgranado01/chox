package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InsurerHireMonitoringInfoProvidedEvent extends BaseActivityEvent {
    public InsurerHireMonitoringInfoProvidedEvent(){};

    public InsurerHireMonitoringInfoProvidedEvent(final Claim claim, String modelName) {
        super(claim, modelName);
        addClaimInsurerHireMonitoringAttributes(claim);
    }
}
