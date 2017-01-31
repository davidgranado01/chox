package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class FullPaymentReceivedEvent extends BaseActivityEvent {
    
    public FullPaymentReceivedEvent(){};

    public FullPaymentReceivedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
