package idas.chox.core.workflow.exceptions;

import idas.chox.core.model.Claim;

/**
 *
 * @author emmanuel
 */
public class InvalidClaimStatusException extends Exception {

    public InvalidClaimStatusException(Claim claim) {
        // 16 Bug Id:79
        // super(String.format("Attemp to process claim with id : %d, but claim status : '%s' is invalid.",claim.getId(),claim.getStatus()));
        super(String.format("Unable to process claim. The claim has been processed by another user. Please see the 'Claim Cycle' tab for further details"));
    }
}
