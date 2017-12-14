package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InsurerHireVehicleUpdatedEvent extends BaseActivityEvent {
    public InsurerHireVehicleUpdatedEvent(){};

    public InsurerHireVehicleUpdatedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addClaimHireVehicleAttributes(claim);
    }
}
