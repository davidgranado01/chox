package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimRoutedEvent extends BaseActivityEvent {
    
    public ClaimRoutedEvent(){};

    public ClaimRoutedEvent(final Claim claim, String activityName, int workgroupId, String workgroupName) {
        super(claim, activityName);
        addAttribute("workgroupId", String.valueOf(workgroupId));
        addAttribute("workgroupName", workgroupName);
    }
}
