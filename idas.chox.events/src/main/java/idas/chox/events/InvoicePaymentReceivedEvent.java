package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoicePaymentReceivedEvent extends BaseActivityEvent {
    public InvoicePaymentReceivedEvent(){};

    public InvoicePaymentReceivedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        this.addInvoicePaidAttributes(claim);
    }
}
