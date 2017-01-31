package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class EcdUpdatedEvent extends BaseActivityEvent {
    
    public EcdUpdatedEvent(){};

    public EcdUpdatedEvent(final Claim claim, String activityName, String ecdDate, String ecdReason, String ecdNote) {
        super(claim, activityName);
        addAttribute("hireMonitoringEcdDate", ecdDate);
        addAttribute("hireMonitoringEcdSupportingNote", ecdNote);
        addAttribute("hireMonitoringEcdReason", ecdReason);
    }
}
