package idas.chox.core.services;

import idas.chox.core.model.Claim;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Invoice;

public interface InvoiceService {

    public void saveInvoiceForXMLUploader(final ClaimResult claimResult);

    public Invoice getInvoice(int invoiceId);

    public void saveInvoice(Invoice invoice);
}
