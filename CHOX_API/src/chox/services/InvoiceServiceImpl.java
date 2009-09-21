package chox.services;

import chox.model.*;

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
