package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.Customer;

public interface CustomerService {

    void saveCustomerForXMLUploader(final ClaimResult claimResult);

    Customer getCustomer(int customerId);

    void saveCustomer(Customer customer);
}
