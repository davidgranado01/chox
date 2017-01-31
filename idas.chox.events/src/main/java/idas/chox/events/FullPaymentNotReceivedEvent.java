package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class FullPaymentNotReceivedEvent extends BaseActivityEvent {
    
    public FullPaymentNotReceivedEvent(){};

    public FullPaymentNotReceivedEvent(final Claim claim, String activityName, String amountReceived) {
        super(claim, activityName);
        addAttribute("amountReceived", amountReceived);
    }
}
