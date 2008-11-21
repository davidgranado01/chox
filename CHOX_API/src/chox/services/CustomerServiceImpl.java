
package chox.services;

import chox.model.*;

public class CustomerServiceImpl {
    
    public static XMLParseResult saveCustomerForXMLUploader(XMLParseResult xmlParseResult){
        
        Customer customer = xmlParseResult.getClaim().getCustomer();
        
        if(customer!=null){
        
            customer.setCreatedBy(WebUserServiceImpl.getCurrentUser());
            customer.setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
            customer.setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
            customer.setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try{
                    xmlParseResult.getCurrentSession().saveOrUpdate(customer);
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
                xmlParseResult.getClaim().setCustomer(customer);
            }
            
        }
        
        return xmlParseResult;
    }
}
