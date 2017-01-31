package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceReferredToEngEvent extends BaseActivityEvent {
    public InvoiceReferredToEngEvent(){};

    public InvoiceReferredToEngEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
