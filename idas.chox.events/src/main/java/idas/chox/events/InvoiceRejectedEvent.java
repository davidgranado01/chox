package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceRejectedEvent extends BaseActivityEvent {
    
    public InvoiceRejectedEvent(){};

    public InvoiceRejectedEvent(final Claim claim, String activityName, String reason, String note) {
        super(claim, activityName);
        addAttribute("rejectionReason", reason);
        addAttribute("supportingNote", note);
    }
    public InvoiceRejectedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
