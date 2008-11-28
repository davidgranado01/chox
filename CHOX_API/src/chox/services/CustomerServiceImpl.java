
package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;

public class CustomerServiceImpl extends DataService implements CustomerService{
    
    public Customer getObject(int id) {
        return (Customer) currentSession.get(Customer.class, id);
    }

    public void updateObject(Customer customer) {

        currentSession.beginTransaction();
        currentSession.update(customer);
        currentSession.getTransaction().commit();
    }

    public XMLParseResult saveCustomerForXMLUploader(XMLParseResult xmlParseResult){
        
        Customer customer = xmlParseResult.getClaim().getCustomer();
        
        if(customer!=null){
        
            customer.setCreatedBy(getCurrentUser().getId());
            customer.setCreatedDate(DateHelper.getCurrentTimeStamp());
            customer.setLastModifiedBy(getCurrentUser().getId());
            customer.setLastModifiedDate(DateHelper.getCurrentTimeStamp());

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
