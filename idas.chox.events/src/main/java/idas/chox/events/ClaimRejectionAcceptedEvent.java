package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimRejectionAcceptedEvent extends BaseActivityEvent {
    public ClaimRejectionAcceptedEvent(){};

    public ClaimRejectionAcceptedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
