package idas.chox.core.services;

import idas.chox.core.model.Claim;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.InvoiceOriginal;

public interface InvoiceService {

    public void saveInvoiceForXMLUploader(final ClaimResult claimResult);

    public Invoice getInvoice(int invoiceId);

    public void saveInvoice(Invoice invoice);
    
    public InvoiceOriginal saveOriginalInvoice(Claim claim, Invoice invoice);
}
