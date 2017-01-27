package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimAcknowledgedEvent extends BaseActivityEvent {
    public ClaimAcknowledgedEvent(){};

    public ClaimAcknowledgedEvent(Claim claim, String activityName) {
        super(claim, activityName);
    }
}
