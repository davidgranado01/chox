/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Invoice;

public interface InvoiceService {
   public void saveObjectForXMLUploader(final ClaimResult claimResult);    
   public Invoice getObject(int id);
   public void updateObject(Invoice invoice);

}
