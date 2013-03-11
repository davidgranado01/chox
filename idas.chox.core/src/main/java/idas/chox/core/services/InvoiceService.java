package idas.chox.core.services;


import idas.chox.core.model.Invoice;
import idas.chox.core.model.InvoiceOriginal;
import idas.chox.core.xmlValidation.ClaimResult;

public interface InvoiceService {

    void saveInvoiceForXMLUploader(final ClaimResult claimResult);

    Invoice getInvoice(int invoiceId);

    void saveInvoice(Invoice invoice);
    
    InvoiceOriginal saveOriginalInvoice(Invoice invoice);
    
    void deleteOriginalInvoice(Invoice invoice);

    int getNoOfRejectedInvoices(Integer reasonOfRejectionId);
 
}
