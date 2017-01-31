package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InterimPaymentUpdatedEvent extends BaseActivityEvent {
    
    public InterimPaymentUpdatedEvent(){};

    public InterimPaymentUpdatedEvent(final Claim claim, String activityName, String additionalInterimPaymentMade, String totalInterimPaymentMade) {
        super(claim, activityName);
        addAttribute("additionalInterimPaymentMade", additionalInterimPaymentMade);
        addAttribute("totalInterimPaymentMade", totalInterimPaymentMade);
    }
}
