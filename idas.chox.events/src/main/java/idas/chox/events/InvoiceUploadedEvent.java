package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceUploadedEvent extends BaseActivityEvent {
    public InvoiceUploadedEvent(){};

    public InvoiceUploadedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addInvoiceAttributes(claim);
    }
}
