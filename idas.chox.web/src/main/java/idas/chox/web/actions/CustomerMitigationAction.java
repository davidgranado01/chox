package idas.chox.web.actions;

import idas.chox.core.model.Customer;
import idas.chox.service.security.ApplicationAccessibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;

/**
 *
 * @author John
 */
public class CustomerMitigationAction extends ClaimModelAction<Customer> {
    
    private static final Logger LOG = LoggerFactory.getLogger(CustomerMitigationAction.class);

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
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("CustomerMitigationAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("CustomerMitigationAction validate success");
        }
        LOG.debug(" CustomerMitigationAction validation is not done as claim is null");
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }

}
