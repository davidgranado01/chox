package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceCreatedEvent extends BaseActivityEvent {
    public InvoiceCreatedEvent(){};

    public InvoiceCreatedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addInvoiceAttributes(claim);
    }
}
