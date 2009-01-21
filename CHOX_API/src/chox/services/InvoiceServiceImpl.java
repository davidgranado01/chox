package chox.services;

import chox.Util.XmlHelper;
import chox.model.*;
import scsbre.engine.*;

public class InvoiceServiceImpl extends DataService implements InvoiceService {

    public RulesEngineResponse XMLUploaderInvoiceValidation(Claim claim) {
        
        RulesEngine r = RulesEngine.getInstance(claim);
        RulesEngineResponse reponse = r.ResolveStatus(); 
        return reponse;
    }

    public XMLParseResult saveInvoiceForXMLUploader(XMLParseResult xmlParseResult) {

        if ((xmlParseResult.getClaim().getInvoice()) != null) {

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try {
                    xmlParseResult.getCurrentSession().saveOrUpdate((xmlParseResult.getClaim().getInvoice()));
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
            }
        }

        return xmlParseResult;
    }

    public Invoice getObject(int id) {
        return (Invoice) getCurrentSession().get(Invoice.class, id);
    }

    public void updateObject(Invoice invoice) {

        getCurrentSession().beginTransaction();
        getCurrentSession().update(invoice);
        getCurrentSession().getTransaction().commit();
    }

}
