package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Customer;

public interface CustomerService {

    public void saveCustomerForXMLUploader(final ClaimResult claimResult);

    public Customer getCustomer(int customerId);

    public void saveCustomer(Customer customer);
}
