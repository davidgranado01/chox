package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class NoteAddedEvent extends BaseActivityEvent {
    
    public NoteAddedEvent(){};

    public NoteAddedEvent(final Claim claim, String activityName, String comment, boolean reviewRequired, int visibility) {
        super(claim, activityName);
        addAttribute("comment", comment);
        addAttribute("reviewRequired", String.valueOf(reviewRequired));
        addAttribute("visibility", String.valueOf(visibility));
    }
}
