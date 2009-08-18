/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Claim;
import chox.model.Invoice;
import chox.xmlValidation.model.ClaimResult;
import scsbre.engine.*;

public interface InvoiceService {
    public void saveObjectForXMLUploader(final ClaimResult claimResult);
    public RulesEngineResponse XMLUploaderInvoiceValidation(Claim claim);
    
   public Invoice getObject(int id);
   public void updateObject(Invoice invoice);

}
