package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimRejectedEvent extends BaseActivityEvent {
    
    public ClaimRejectedEvent(){};

    public ClaimRejectedEvent(final Claim claim, String activityName, String reason, String note) {
        super(claim, activityName);
        addAttribute("rejectionReason", reason);
        addAttribute("supportingNote", note);
    }
}
