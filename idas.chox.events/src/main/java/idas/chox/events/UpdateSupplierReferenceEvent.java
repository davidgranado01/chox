package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class UpdateSupplierReferenceEvent extends BaseActivityEvent {
    public UpdateSupplierReferenceEvent(){};

    public UpdateSupplierReferenceEvent(final Claim claim, String activityName, String oldSupplierReference) {
        super(claim, activityName);
        addAttribute("originalChoReference", oldSupplierReference);
        addAttribute("choReference", claim.getChoReference());
    }
}
