package idas.chox.web.actions;

import java.util.Date;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.service.security.TabAccessibility;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Emmanuel
 */
public class CustomerVehicleDamageAction extends ClaimModelAction<Customer> {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerVehicleDamageAction.class);
    private Boolean isUsableOriginal = null;
    private Boolean isTotalLossOriginal = null;

    @Override
    public Customer loadModel() {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            isTotalLossOriginal = customer.getIsTotalLoss();
            isUsableOriginal = customer.getIsUsable();
            return customer;
        }
        return new Customer();
    }

   @Override
   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public String updateModel() {
        LOG.debug("Updating Vehicle Damage - total loss (original) = '{}', total loss (model) = '{}'", isTotalLossOriginal, model.getIsTotalLoss());
        // If total loss has changed, we also need to update the original field
        // and the hire monitoring total loss fields
        if (isTotalLossOriginal != model.getIsTotalLoss()) {
            HireMonitoringDetail hireMonDetail = claim.getHireMonitoringDetail();
            if (hireMonDetail == null) {
                hireMonDetail = new HireMonitoringDetail();
            }
            hireMonDetail.setIsTotalLostCheck(model.getIsTotalLoss());
            hireMonDetail.setIsTotalLostCheckLastModified(new Date());
            claim.setHireMonitoringDetail(hireMonDetail);
            if (model.getIsTotalLossOriginal() == null) {
                model.setIsTotalLossOriginal(isTotalLossOriginal);
            }
        }
        claim.setCustomer(model);

        // If the 'is usable' status has changed then we need to check for anomalies
        boolean updated = false;
        if (isUsableOriginal != model.getIsUsable()) {
            claimService.checkRepairBookedInDateAnomaly(claim);
            updated=true;
            
        }
        if (isTotalLossOriginal != model.getIsTotalLoss()) {
            claimService.checkTotalLossAnomaly(claim);
            updated=true;
        }
        
        if (updated) {
            // update model in session before calling super.updateModel as model version
            // may have been increased when anomalous added or removed from claim.
            updateModelInSession(Arrays.asList(claim, model, claim.getHireMonitoringDetail()));
        }
        return super.updateModel();
    }
    
    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("CustomerVehicleDamageAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("CustomerVehicleDamageAction validate success");
        }
        else {
            LOG.debug(" CustomerVehicleDamageAction validation is not done as claim is null");
        }
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }
}

