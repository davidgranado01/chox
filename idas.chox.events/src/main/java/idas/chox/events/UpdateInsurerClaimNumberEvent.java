package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class UpdateInsurerClaimNumberEvent extends BaseActivityEvent {
    public UpdateInsurerClaimNumberEvent(){};

    public UpdateInsurerClaimNumberEvent(final Claim claim, String activityName, String claimNumber) {
        super(claim, activityName);
        addAttribute("claimNumber", claimNumber);
    }
}
