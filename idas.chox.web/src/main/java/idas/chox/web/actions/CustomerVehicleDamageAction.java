package idas.chox.web.actions;

import idas.chox.core.model.Customer;
import idas.chox.service.security.ApplicationAccessibility;

/**
 *
 * @author Emmanuel
 */
public class CustomerVehicleDamageAction extends ClaimModelAction<Customer> {

    @Override
    public Customer loadModel() {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            return customer;
        }
        return new Customer();
    }

    @Override
    public String updateModel() {
        claim.setCustomer(model);
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }
}

