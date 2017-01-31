package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class SlaExtensionGrantedEvent extends BaseActivityEvent {
    
    public SlaExtensionGrantedEvent(){};

    public SlaExtensionGrantedEvent(final Claim claim, String activityName, String extensionDays) {
        super(claim, activityName);
        addAttribute("extensionDays", extensionDays);
    }
}
