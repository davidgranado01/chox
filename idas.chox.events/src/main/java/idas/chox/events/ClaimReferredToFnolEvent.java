package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimReferredToFnolEvent extends BaseActivityEvent {
    public ClaimReferredToFnolEvent(){};

    public ClaimReferredToFnolEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
