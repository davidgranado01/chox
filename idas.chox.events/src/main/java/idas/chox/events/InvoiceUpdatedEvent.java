package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceUpdatedEvent extends BaseActivityEvent {
    public InvoiceUpdatedEvent(){};

    public InvoiceUpdatedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addInvoiceAttributes(claim);
    }
}
