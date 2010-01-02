package idas.chox.data.services;

import idas.chox.core.model.Invoice;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.xmlValidation.ClaimResult;

public class InvoiceServiceImpl extends SecureDataService implements InvoiceService {

    public void saveInvoiceForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getInvoice()) != null) {
            getHibernateTemplate().saveOrUpdate((claimResult.getClaim().getInvoice()));
        }
    }

    public Invoice getInvoice(int id) {
        return (Invoice) get(Invoice.class, id);
    }

    public void saveInvoice(Invoice invoice) {

        save(invoice);
    }
}
