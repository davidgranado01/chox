package idas.chox.data.services;

import idas.chox.core.model.Invoice;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class InvoiceServiceImpl extends SecureDataService implements InvoiceService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveInvoiceForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getInvoice()) != null) {
            getHibernateTemplate().saveOrUpdate((claimResult.getClaim().getInvoice()));
        }
    }

    public Invoice getInvoice(int id) {
        return (Invoice) get(Invoice.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveInvoice(Invoice invoice) {

        save(invoice);
    }
}
