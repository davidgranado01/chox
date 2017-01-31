package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceSubmittedEvent extends BaseActivityEvent {
    public InvoiceSubmittedEvent(){};

    public InvoiceSubmittedEvent(final Claim claim, final String activityName) {
        super(claim, activityName);
        addInvoiceAttributes(claim);
    }
}
