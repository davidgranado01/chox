package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimReferredToEngEvent extends BaseActivityEvent {
    public ClaimReferredToEngEvent(){};

    public ClaimReferredToEngEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
