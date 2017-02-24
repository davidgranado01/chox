package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimMatchingEvent extends BaseActivityEvent {
    public ClaimMatchingEvent(){};

    public ClaimMatchingEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
