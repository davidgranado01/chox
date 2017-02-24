package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimResubmittedEvent extends BaseActivityEvent {
    public ClaimResubmittedEvent(){};

    public ClaimResubmittedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addClaimAttributes(claim);
    }
}
