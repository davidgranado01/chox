package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class NewClaimEvent extends BaseActivityEvent {
    public NewClaimEvent(){};

    public NewClaimEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addClaimAttributes(claim);
    }
}
