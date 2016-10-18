package idas.chox.web.actions;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Customer;
import idas.chox.service.security.TabAccessibility;
import java.util.Objects;


/**
 *
 * @author Emmanuel
 */
public class CustomerVehicleDamageAction extends ClaimModelAction<Customer> {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerVehicleDamageAction.class);
    private Boolean isUsableOriginal = null;
    private Boolean isTotalLossOriginal = null;
    private Boolean isTotalLossNew = null;

    public Boolean getIsTotalLossNew() {
        return isTotalLossNew;
    }

    public void setIsTotalLossNew(Boolean isTotalLossNew) {
        this.isTotalLossNew = isTotalLossNew;
    }

    
    @Override
    public Customer loadModel() {
        Customer customer = claim.getCustomer();
        if (customer != null) {
            isTotalLossOriginal = customer.getIsTotalLoss();
            isUsableOriginal = customer.getIsUsable();
            isTotalLossNew = customer.getIsTotalLoss();
            
            return customer;
        }
        return new Customer();
    }

   @Override
   public String updateModel() {
        boolean updated = false;
        LOG.debug("Updating Vehicle Damage - total loss (original) = '{}', total loss (model) = '{}'", isTotalLossOriginal, model.getIsTotalLoss());
        // If total loss has changed, we also need to update the original field
        // and the hire monitoring total loss fields
        if (!Objects.equals(isTotalLossOriginal, isTotalLossNew)) {
            claimService.setTotalLoss(claim, isTotalLossNew);
            updated=true;
        }
        claim.setCustomer(model);

        // If the 'is usable' status has changed then we need to check for anomalies
        if (!Objects.equals(isUsableOriginal, model.getIsUsable())) {
            claimService.checkRepairBookedInDateAnomaly(claim);
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
    
    public void setIsUsable(String isUsable) {
LOG.info("setIsUsable: {}", isUsable);
        if ("Yes".equalsIgnoreCase(isUsable) || "True".equalsIgnoreCase(isUsable)) {
            model.setIsUsable(Boolean.TRUE);
        } else if ("No".equalsIgnoreCase(isUsable) || "False".equalsIgnoreCase(isUsable)) {
            model.setIsUsable(Boolean.FALSE);
        } else {
            model.setIsUsable((Boolean)null);
        }
    }
}

