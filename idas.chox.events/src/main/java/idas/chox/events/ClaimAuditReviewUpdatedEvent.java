package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimAuditReviewUpdatedEvent extends BaseActivityEvent {
    
    public ClaimAuditReviewUpdatedEvent(){};

    public ClaimAuditReviewUpdatedEvent(final Claim claim, String activityName) {
        super(claim, activityName);
        addClaimAuditReviewAttributes(claim);
    }
}
