package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class SubscriberClaimRejectedToGtaEvent extends BaseActivityEvent {
    public SubscriberClaimRejectedToGtaEvent(){};

    public SubscriberClaimRejectedToGtaEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
