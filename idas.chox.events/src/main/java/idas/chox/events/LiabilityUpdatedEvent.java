package idas.chox.events;

import idas.chox.core.model.Claim;
import idas.chox.core.model.LiabilityStatus;

/**
 *
 * @author john
 */
public class LiabilityUpdatedEvent extends BaseActivityEvent {
    
    public LiabilityUpdatedEvent(){};

    public LiabilityUpdatedEvent(final Claim claim, String activityName, String supportingNote) {
        super(claim, activityName);
        addAttribute("supportingNote", supportingNote);
        this.addClaimLiabilityAttributes(claim);
    }
}
