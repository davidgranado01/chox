package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InvoiceSwitchedFromPaymentsTeamEvent extends BaseActivityEvent {
    public InvoiceSwitchedFromPaymentsTeamEvent(){};

    public InvoiceSwitchedFromPaymentsTeamEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
