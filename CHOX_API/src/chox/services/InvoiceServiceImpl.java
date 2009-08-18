package chox.services;

import chox.model.*;
import chox.xmlValidation.model.ClaimResult;
import scsbre.engine.*;

public class InvoiceServiceImpl extends SecureDataService implements InvoiceService {

    public RulesEngineResponse XMLUploaderInvoiceValidation(Claim claim) {

        RulesEngine r = RulesEngine.getInstance(claim);
        RulesEngineResponse reponse = r.ResolveStatus();
        return reponse;
    }

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
