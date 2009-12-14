package idas.chox.data.services;

import idas.chox.core.model.Invoice;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.xmlValidation.ClaimResult;

public class InvoiceServiceImpl extends SecureDataService implements InvoiceService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getInvoice()) != null) {
            getHibernateTemplate().saveOrUpdate((claimResult.getClaim().getInvoice()));
        }
    }

    public Invoice getObject(int id) {
        return (Invoice) get(Invoice.class, id);
    }

    public void updateObject(Invoice invoice) {

        save(invoice);
    }
}
