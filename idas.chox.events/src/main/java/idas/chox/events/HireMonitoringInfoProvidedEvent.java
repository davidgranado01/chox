package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class HireMonitoringInfoProvidedEvent extends BaseActivityEvent {
    public HireMonitoringInfoProvidedEvent(){};

    public HireMonitoringInfoProvidedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addClaimHireMonitoringAttributes(claim);
    }
}
