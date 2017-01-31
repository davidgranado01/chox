package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimRejectionContestedEvent extends BaseActivityEvent {
    public ClaimRejectionContestedEvent(){};

    public ClaimRejectionContestedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
