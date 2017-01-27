package idas.chox.events;

import idas.chox.core.model.Claim;
import idas.chox.core.model.LiabilityStatus;

/**
 *
 * @author john
 */
public class LiabilityUpdatedEvent extends BaseActivityEvent {
    
    public LiabilityUpdatedEvent(){};

    public LiabilityUpdatedEvent(Claim claim, String activityName, LiabilityStatus liabilityStatus) {
        super(claim, activityName);
        this.addAttribute("liabilityStatus", liabilityStatus.toString());
    }
    
    @Override
    public String toString() {
        return this.getActivityName() + "(" + this.getAttributes().get("liabilityStatus") + ")";
    }
}
