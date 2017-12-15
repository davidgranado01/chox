package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class InsurerEcdUpdatedEvent extends BaseActivityEvent {
    
    public InsurerEcdUpdatedEvent(){};

    public InsurerEcdUpdatedEvent(final Claim claim, String activityName, String ecdDate, String ecdReason, String ecdNote) {
        super(claim, activityName);
        addAttribute("insurerHireMonitoringEcdDate", ecdDate);
        addAttribute("insurerHireMonitoringEcdSupportingNote", ecdNote);
        addAttribute("insurerHireMonitoringEcdReason", ecdReason);
    }
}
