package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimReviewedByEngEvent extends BaseActivityEvent {
    public ClaimReviewedByEngEvent(){};

    public ClaimReviewedByEngEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
