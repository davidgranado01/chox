package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InsurerOwnerAssignedEvent extends BaseActivityEvent {
    public InsurerOwnerAssignedEvent(){};

    public InsurerOwnerAssignedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addAttribute("insurerOwnerName", claim.getClaimOwner() == null ? "" : claim.getClaimOwner().getFullName());
    }
}
