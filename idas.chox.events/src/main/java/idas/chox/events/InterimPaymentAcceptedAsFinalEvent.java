package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InterimPaymentAcceptedAsFinalEvent extends BaseActivityEvent {
    
    public InterimPaymentAcceptedAsFinalEvent(){};

    public InterimPaymentAcceptedAsFinalEvent(final Claim claim, String activityName, String amountReceived) {
        super(claim, activityName);
        addAttribute("amountReceived", amountReceived);
    }
}
