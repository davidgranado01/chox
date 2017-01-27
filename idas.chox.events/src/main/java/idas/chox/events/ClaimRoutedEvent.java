package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimRoutedEvent extends BaseActivityEvent {
    
    public ClaimRoutedEvent(){};

    public ClaimRoutedEvent(Claim claim, String activityName, int workgroupId, String workgroupName) {
        super(claim, activityName);
        this.addAttribute("workgroupId", String.valueOf(workgroupId));
        this.addAttribute("workgroupName", workgroupName);
    }
    
    @Override
    public String toString() {
        return this.getActivityName() + "(" + this.getAttributes().get("workgroupName") + ")";
    }
}
