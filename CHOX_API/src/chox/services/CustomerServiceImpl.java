
package chox.services;

import chox.model.*;
import chox.xmlValidation.model.ClaimResult;

public class CustomerServiceImpl extends SecureDataService implements CustomerService{
    
    public Customer getObject(int id) {
        return (Customer) get(Customer.class, id);
    }

    public void updateObject(Customer customer) {
        save(customer);
    }

    public void saveObjectForXMLUploader(final ClaimResult claimResult){
        Customer customer = claimResult.getClaim().getCustomer();
        if(customer!=null){       
            getHibernateTemplate().saveOrUpdate(customer);
        }
    }
}
