/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Invoice;

public interface InvoiceService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult);

    public Invoice getObject(int id);

    public void updateObject(Invoice invoice);
}
