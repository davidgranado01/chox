package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ChoOwnerAssignedEvent extends BaseActivityEvent {
    public ChoOwnerAssignedEvent(){};

    public ChoOwnerAssignedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addAttribute("choOwnerName", claim.getSupplierClaimOwner() == null ? "" : claim.getSupplierClaimOwner().getFullName());
    }
}
