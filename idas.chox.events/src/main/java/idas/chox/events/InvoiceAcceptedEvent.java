package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceAcceptedEvent extends BaseActivityEvent {
    public InvoiceAcceptedEvent(){};

    public InvoiceAcceptedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
