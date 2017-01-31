package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimPendingEvent extends BaseActivityEvent {
    public ClaimPendingEvent(){};

    public ClaimPendingEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
