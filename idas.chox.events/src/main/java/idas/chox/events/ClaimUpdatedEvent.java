package idas.chox.events;

import idas.chox.core.model.Claim;

/**
 *
 * @author john
 */
public class ClaimUpdatedEvent extends BaseActivityEvent {
    public ClaimUpdatedEvent(){};

    public ClaimUpdatedEvent(final Claim claim, String modelName) {
        super(claim, modelName);
        addClaimAttributes(claim);
    }
}
