package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;
import scsbre.engine.*;

public class InvoiceServiceImpl  extends DataService implements InvoiceService{
    
    public RulesEngineResponse XMLUploaderInvoiceValidation(Claim claim){
        
        RulesEngine r = RulesEngine.getInstance(claim);
        RulesEngineResponse reponse = r.ResolveStatus();     
        
        HistoryService historyService = new HistoryServiceImpl();
        historyService.logInvoiceValidationErrorMsg(reponse, claim);
        
        return reponse;
    }
    
    public XMLParseResult saveInvoiceForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getClaim().getInvoice())!=null){
            

                (xmlParseResult.getClaim().getInvoice()).setCreatedBy(getCurrentUser().getId());
                (xmlParseResult.getClaim().getInvoice()).setCreatedDate(DateHelper.getCurrentTimeStamp());
                (xmlParseResult.getClaim().getInvoice()).setLastNodifiedBy(getCurrentUser().getId());
                (xmlParseResult.getClaim().getInvoice()).setLastModifiedDate(DateHelper.getCurrentTimeStamp());

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
