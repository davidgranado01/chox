package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceReferredToCHEvent extends BaseActivityEvent {
    public InvoiceReferredToCHEvent(){};

    public InvoiceReferredToCHEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
