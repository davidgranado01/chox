package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimSwitchedToGtaEvent extends BaseActivityEvent {
    public ClaimSwitchedToGtaEvent(){};

    public ClaimSwitchedToGtaEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
