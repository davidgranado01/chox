package idas.chox.core.workflow.exceptions;

import idas.chox.core.model.Claim;

/**
 *
 * @author emmanuel
 */
public class InvalidClaimStatusException extends Exception {

    public InvalidClaimStatusException(Claim claim) {
        super(String.format("Unable to process claim '%s'. This claim may have been processed by another user. Please see the 'Claim Cycle' tab for further details",
                claim.getChoReference()));
    }
}
