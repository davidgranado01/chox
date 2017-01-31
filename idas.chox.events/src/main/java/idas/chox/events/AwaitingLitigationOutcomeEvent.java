package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class AwaitingLitigationOutcomeEvent extends BaseActivityEvent {
    public AwaitingLitigationOutcomeEvent(){};

    public AwaitingLitigationOutcomeEvent(final Claim claim, String activityName) {
        super(claim, activityName);
    }
}
