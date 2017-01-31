package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimRegisteredByFnolEvent extends BaseActivityEvent {
    public ClaimRegisteredByFnolEvent(){};

    public ClaimRegisteredByFnolEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
