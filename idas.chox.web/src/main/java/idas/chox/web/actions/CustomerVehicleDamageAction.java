package idas.chox.web.actions;

import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Emmanuel
 */
public class CustomerVehicleDamageAction extends ClaimModelAction<Customer> {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerVehicleDamageAction.class);
    private Boolean isTotalLoss = null;
    private Boolean isTotalLossOriginal = null;

    @Override
    public Customer loadModel() {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            isTotalLoss = customer.getIsTotalLoss();
            isTotalLossOriginal = customer.getIsTotalLossOriginal();
            return customer;
        }
        return new Customer();
    }

    @Override
    public String updateModel() {
        LOG.debug("Updating Vehicle Damage - total loss (original) = '{}', total loss (model) = '{}'", isTotalLoss, model.getIsTotalLoss());
        if (isTotalLossOriginal == null && isTotalLoss != model.getIsTotalLoss()) {
            // isTotalLoss has changed and so we have to store the original value
            model.setIsTotalLossOriginal(isTotalLoss);
        }
        // If total loss has changed, we also need to update the hire monitoring total loss field
        if (isTotalLoss != model.getIsTotalLoss()) {
            HireMonitoringDetail hireMonDetail = claim.getHireMonitoringDetail();
            hireMonDetail.setIsTotalLostCheck(model.getIsTotalLoss());
            hireMonDetail.setIsTotalLostCheckLastModified(new Date());
            claim.setHireMonitoringDetail(hireMonDetail);
        }
        claim.setCustomer(model);
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }
}

