package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoicePaidEvent extends BaseActivityEvent {
    public InvoicePaidEvent(){};

    public InvoicePaidEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        this.addInvoicePaidAttributes(claim);
    }
}
