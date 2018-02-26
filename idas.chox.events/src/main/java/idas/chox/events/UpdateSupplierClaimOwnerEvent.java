package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class UpdateSupplierClaimOwnerEvent extends BaseActivityEvent {
    public UpdateSupplierClaimOwnerEvent(){};

    public UpdateSupplierClaimOwnerEvent(final Claim claim, String activityName, String oldSupplierClaimOwner) {
        super(claim, activityName);
        addAttribute("oldSupplierClaimOwner", oldSupplierClaimOwner);
        addAttribute("supplierClaimOwner", claim.getSupplierClaimOwner().getDisplayName());
    }
}
