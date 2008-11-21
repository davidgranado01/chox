package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;

public class InvoiceServiceImpl implements InvoiceService{

    public XMLParseResult saveInvoiceForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getClaim().getInvoice())!=null){
            

                (xmlParseResult.getClaim().getInvoice()).setCreatedBy(WebUserServiceImpl.getCurrentUser());
                (xmlParseResult.getClaim().getInvoice()).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
                (xmlParseResult.getClaim().getInvoice()).setLastNodifiedBy(WebUserServiceImpl.getCurrentUser());
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
