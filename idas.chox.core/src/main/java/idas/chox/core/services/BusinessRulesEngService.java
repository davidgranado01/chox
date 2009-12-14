/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.xmlValidation.*;
import idas.chox.core.model.Claim;

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
