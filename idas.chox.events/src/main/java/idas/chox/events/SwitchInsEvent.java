package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class SwitchInsEvent extends BaseActivityEvent {
    
    public SwitchInsEvent(){};

    public SwitchInsEvent(final Claim claim, String activityName, Integer insId, String newIns) {
        super(claim, activityName);
        this.setInsurerId(insId);
        addAttribute("newInsurerName", newIns);
    }
}
