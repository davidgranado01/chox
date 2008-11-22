package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;
import scsbre.engine.*;
import java.util.List;

public class InvoiceServiceImpl  extends DataService implements InvoiceService{
    
    public RulesEngineResponse XMLUploaderInvoiceValidation(Claim claim){
        
        RulesEngine r = RulesEngine.getInstance(claim);
        RulesEngineResponse reponse = r.ResolveStatus();     
        
        HistoryService historyService = new HistoryServiceImpl();
        historyService.logInvoiceValidationErrorMsg(reponse, claim.getId());
        
        return reponse;
    }
    
    public XMLParseResult saveInvoiceForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getClaim().getInvoice())!=null){
            

                (xmlParseResult.getClaim().getInvoice()).setCreatedBy(getCurrentUser().getId());
                (xmlParseResult.getClaim().getInvoice()).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
                (xmlParseResult.getClaim().getInvoice()).setLastNodifiedBy(getCurrentUser().getId());
                (xmlParseResult.getClaim().getInvoice()).setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try{
                        xmlParseResult.getCurrentSession().saveOrUpdate((xmlParseResult.getClaim().getInvoice()));
                    } catch (Exception e) {
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                    }
                }
            }
        
        return xmlParseResult;
    }     

}
