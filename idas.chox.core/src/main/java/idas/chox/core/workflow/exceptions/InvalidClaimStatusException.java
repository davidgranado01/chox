/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.workflow.exceptions;

import idas.chox.core.model.Claim;

/**
 *
 * @author emmanuel
 */
public class InvalidClaimStatusException extends Exception {

    public InvalidClaimStatusException(Claim claim) {
        super(String.format("Attemp to process claim with id : %d, but claim status : '%s' is invalid.",claim.getId(),claim.getStatus()));
    }
}
