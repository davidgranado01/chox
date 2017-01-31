package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimNumberUpdatedEvent extends BaseActivityEvent {
    
    public ClaimNumberUpdatedEvent(){};

    public ClaimNumberUpdatedEvent(final Claim claim, String activityName, String claimNumber) {
        super(claim, activityName);
        addAttribute("claimNumber", claimNumber);
    }
}
