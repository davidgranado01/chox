package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimClosedEvent extends BaseActivityEvent {
    
    public ClaimClosedEvent(){};

    public ClaimClosedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
    public ClaimClosedEvent(final Claim claim, String activityName, Integer insurerId) {
        super(claim, activityName);
        this.setInsurerId(insurerId);
    }
}
