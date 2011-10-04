package idas.chox.web.actions;

import idas.chox.core.model.Customer;
import idas.chox.service.security.ApplicationAccessibility;
import org.springframework.security.AccessDeniedException;

/**
 *
 * @author John
 */
public class CustomerMitigationAction extends ClaimModelAction<Customer> {

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
        if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
            throw new AccessDeniedException("Attempt to access a claim that you do not own.");
        }
        claim.setCustomer(model);
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }

}
