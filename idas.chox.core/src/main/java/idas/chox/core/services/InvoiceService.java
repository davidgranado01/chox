package idas.chox.core.services;


import idas.chox.core.model.Invoice;
import idas.chox.core.model.InvoiceOriginal;
import idas.chox.core.xmlValidation.ClaimResult;

public interface InvoiceService {

    public void saveInvoiceForXMLUploader(final ClaimResult claimResult);

    public Invoice getInvoice(int invoiceId);

    public void saveInvoice(Invoice invoice);
    
    public InvoiceOriginal saveOriginalInvoice(Invoice invoice);
    
    public void deleteOriginalInvoice(Invoice invoice);

    public int getNoOfRejectedInvoices(Integer reasonOfRejectionId);
 
}
