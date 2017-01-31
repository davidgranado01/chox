package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class SwitchChoEvent extends BaseActivityEvent {
    
    public SwitchChoEvent(){};

    public SwitchChoEvent(final Claim claim, String activityName, Integer id, String oldCho, String newCho) {
        super(claim, activityName);
        this.setChoId(id);
        addAttribute("oldCho", oldCho);
        addAttribute("newCho", newCho);
    }
}
