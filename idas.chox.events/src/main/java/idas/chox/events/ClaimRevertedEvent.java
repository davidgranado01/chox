package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimRevertedEvent extends BaseActivityEvent {
    public ClaimRevertedEvent(){};

    public ClaimRevertedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
