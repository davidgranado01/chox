package idas.chox.data.services;

import idas.chox.core.model.Customer;
import idas.chox.core.services.CustomerService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class CustomerServiceImpl extends SecureDataService implements CustomerService {

    @Override
    public Customer getCustomer(int id) {
        return (Customer) get(Customer.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveCustomer(Customer customer) {
        save(customer);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveCustomerForXMLUploader(final ClaimResult claimResult) {
        Customer customer = claimResult.getClaim().getCustomer();
        if (customer != null) {
            getHibernateTemplate().saveOrUpdate(customer);
        }
    }
}
