package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceRejectionContestedEvent extends BaseActivityEvent {
    public InvoiceRejectionContestedEvent(){};

    public InvoiceRejectionContestedEvent(final Claim claim, String activityName, String notes) {
        super(claim, activityName);
        addAttribute("supportingNote", notes);
    }
}
