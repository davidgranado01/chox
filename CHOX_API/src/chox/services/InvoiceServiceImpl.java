package chox.services;

import chox.model.*;
import scsbre.engine.*;

public class InvoiceServiceImpl extends DataService implements InvoiceService {

    public RulesEngineResponse XMLUploaderInvoiceValidation(Claim claim) {

        RulesEngine r = RulesEngine.getInstance(claim);
        RulesEngineResponse reponse = r.ResolveStatus();
        return reponse;
    }

    public void saveObjectForXMLUploader(final XMLParseResult xmlParseResult) {

        if ((xmlParseResult.getClaim().getInvoice()) != null) {
            getHibernateTemplate().saveOrUpdate((xmlParseResult.getClaim().getInvoice()));
        }
    }

    public Invoice getObject(int id) {
        return (Invoice) get(Invoice.class, id);
    }

    public void updateObject(Invoice invoice) {

        save(invoice);
    }
}
