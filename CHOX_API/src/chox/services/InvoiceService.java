/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.XMLParseResult;
import chox.model.Claim;
import chox.model.Invoice;
import scsbre.engine.*;

public interface InvoiceService {
    public void saveObjectForXMLUploader(final XMLParseResult xmlParseResult);
    public RulesEngineResponse XMLUploaderInvoiceValidation(Claim claim);
    
   public Invoice getObject(int id);
   public void updateObject(Invoice invoice);

}
