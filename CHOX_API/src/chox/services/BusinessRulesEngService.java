/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.Claim;
import scsbre.engine.RulesEngineResponse;

/**
 *
 * @author emmanuel
 */
public interface BusinessRulesEngService {

    // XML UPLOAD
    ClaimResult execute(ClaimResult claimResult);
    
    // RulesEngineResponse validate(Claim claim);
    // Claim constructBreValidateObject(Claim claim);

    // INVOICE RESUBMIT
    RulesEngineResponse processResubmitInvoice(Claim claim);

}
