package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class HireVehicleUpdatedEvent extends BaseActivityEvent {
    public HireVehicleUpdatedEvent(){};

    public HireVehicleUpdatedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addClaimHireVehicleAttributes(claim);
    }
}
