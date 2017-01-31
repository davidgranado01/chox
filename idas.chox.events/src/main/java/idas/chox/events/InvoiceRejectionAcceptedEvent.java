package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceRejectionAcceptedEvent extends BaseActivityEvent {
    public InvoiceRejectionAcceptedEvent(){};

    public InvoiceRejectionAcceptedEvent(final Claim claim, String activityName, String notes) {
        super(claim, activityName);
        addAttribute("supportingNote", notes);
    }
}
