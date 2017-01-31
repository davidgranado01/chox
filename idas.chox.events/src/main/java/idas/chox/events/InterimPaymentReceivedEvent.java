package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InterimPaymentReceivedEvent extends BaseActivityEvent {
    
    public InterimPaymentReceivedEvent(){};

    public InterimPaymentReceivedEvent(final Claim claim, String activityName, String amountReceived) {
        super(claim, activityName);
        addAttribute("amountReceived", amountReceived);
    }
}
