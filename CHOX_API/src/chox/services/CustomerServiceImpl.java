
package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;

public class CustomerServiceImpl {
    
    public static XMLParseResult saveCustomerForXMLUploader(Session currentSession, XMLParseResult xmlParseResult){
        
        Customer customer = xmlParseResult.getClaim().getCustomer();
        
        if(customer!=null){
        
            customer.setCreatedBy(WebUserServiceImpl.getCurrentUser());
            customer.setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
            customer.setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
            customer.setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {
                try{
                    currentSession.saveOrUpdate(customer);
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
                xmlParseResult.getClaim().setCustomer(customer);
            }
            
        }
        
        return xmlParseResult;
    }
}
