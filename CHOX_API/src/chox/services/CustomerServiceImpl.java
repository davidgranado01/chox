
package chox.services;

import chox.model.*;

public class CustomerServiceImpl extends SecureDataService implements CustomerService{
    
    public Customer getObject(int id) {
        return (Customer) get(Customer.class, id);
    }

    public void updateObject(Customer customer) {
        save(customer);
    }

    public void saveObjectForXMLUploader(final XMLParseResult xmlParseResult){
        
        Customer customer = xmlParseResult.getClaim().getCustomer();
        
        if(customer!=null){       
         
            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {                
                    getHibernateTemplate().saveOrUpdate(customer);
            }            
        }
    }
}
