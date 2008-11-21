/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.XMLParseResult;

public interface InvoiceService {
    public XMLParseResult saveInvoiceForXMLUploader(XMLParseResult xmlParseResult);

}
