package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class UpdateCaseWithSolicitorEvent extends BaseActivityEvent {
    public UpdateCaseWithSolicitorEvent(){};

    public UpdateCaseWithSolicitorEvent(final Claim claim, String activityName, String caseWithSolicitor) {
        super(claim, activityName);
        addAttribute("caseWithSolicitor", caseWithSolicitor);
    }
}
