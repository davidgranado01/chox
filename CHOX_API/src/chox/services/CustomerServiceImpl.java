
package chox.services;

import chox.model.*;
import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class CustomerServiceImpl implements CustomerService{
    
    public Customer getCustomerById(int id){
        
        Session currentSession = HibernateUtil.currentSession();      
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
