package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimNumberUpdatedEvent extends BaseActivityEvent {
    
    public ClaimNumberUpdatedEvent(){};

    public ClaimNumberUpdatedEvent(Claim claim, String activityName, String claimNumber) {
        super(claim, activityName);
        this.addAttribute("claimNumber", claimNumber);
    }
    
    @Override
    public String toString() {
        return this.getActivityName() + "(" + this.getAttributes().get("claimNumber") + ")";
    }
}
