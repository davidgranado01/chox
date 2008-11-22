
package chox.services;

import chox.model.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class CustomerServiceImpl  extends DataService implements CustomerService{
    
    public Customer getCustomerById(int id){        
        
        Customer customer = new Customer();
        
        try {
            Criteria criteria = currentSession.createCriteria(Customer.class);
            criteria.add(Restrictions.eq("id", id));
            customer = (Customer) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        return customer;
    }

    public XMLParseResult saveCustomerForXMLUploader(XMLParseResult xmlParseResult){
        
        Customer customer = xmlParseResult.getClaim().getCustomer();
        
        if(customer!=null){
        
            customer.setCreatedBy(getCurrentUser().getId());
            customer.setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
            customer.setLastModifiedBy(getCurrentUser().getId());
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
