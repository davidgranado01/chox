package idas.chox.data.services;

import idas.chox.core.model.Customer;
import idas.chox.core.services.CustomerService;
import idas.chox.core.xmlValidation.ClaimResult;

public class CustomerServiceImpl extends SecureDataService implements CustomerService {

    public Customer getObject(int id) {
        return (Customer) get(Customer.class, id);
    }

    
    public void updateObject(Customer customer) {
        save(customer);
    }

    public void saveObjectForXMLUploader(final ClaimResult claimResult) {
        Customer customer = claimResult.getClaim().getCustomer();
        if (customer != null) {
            getHibernateTemplate().saveOrUpdate(customer);
        }
    }
}
