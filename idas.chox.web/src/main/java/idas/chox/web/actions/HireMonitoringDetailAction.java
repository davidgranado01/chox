package idas.chox.web.actions;

import java.util.Arrays;
import java.util.List;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.services.LookupService;
import idas.chox.service.security.TabAccessibility;
import idas.chox.data.events.ActivityEvent;
import idas.chox.service.workflow.activities.ActivityEventGenerator;

/**
 *
 * @author John
 */
public class HireMonitoringDetailAction extends ClaimModelAction<HireMonitoringDetail> {
    
    private static final Logger LOG = LoggerFactory.getLogger(HireMonitoringDetailAction.class);
    private List nonProvisionReasons;
    private LookupService lookupService;
    private Boolean isTotalLossOriginal;
    private Date repairBookedInDateOriginal;
    private String labourRate;
    private String labourHour;
    private String labourCost;
    private boolean managingRepair;
//    private ActivityEventGenerator eventGenerator;
    
    public String getLabourCost() {
        return labourCost;
    }
    
    public void setLabourCost(String labourCost) {
        this.labourCost = labourCost;
    }
    
    public String getLabourHour() {
        return labourHour;
    }
    
    public void setLabourHour(String labourHour) {
        this.labourHour = labourHour;
    }
    
    public String getLabourRate() {
        return labourRate;
    }
    
    public void setLabourRate(String labourRate) {
        this.labourRate = labourRate;
    }
    
    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public boolean isManagingRepair() {
        return claim.isManagingRepair();
    }

    public String getManagingRepairOriginalDesc() {
        if (claim.getManagingRepairOriginal() == null) {
            return "";
        }
        else {
            return claim.getManagingRepairOriginal() ? "(Yes)" : "(No)";
        }
    }
    
    public void setManagingRepair(boolean managingRepair) {
        this.managingRepair = managingRepair;
    }

    public Date getManagingRepairLastModified() {
        return claim.getManagingRepairLastModified();
    }

//    public void setEventGenerator(ActivityEventGenerator eventGenerator) {
//        this.eventGenerator = eventGenerator;
//    }

    @Override
    public HireMonitoringDetail loadModel() {
        
        HireMonitoringDetail hireMonitoringDetail = claim.getHireMonitoringDetail();
        if (hireMonitoringDetail != null) {
            isTotalLossOriginal = hireMonitoringDetail.isIsTotalLostCheck();
            repairBookedInDateOriginal = hireMonitoringDetail.getRepairBookInDate();
            return hireMonitoringDetail;
        }
        isTotalLossOriginal = false;
        return new HireMonitoringDetail();
    }
    
    @Override
    public String updateModel() {
        try {
            checkVersion(Arrays.asList(claim,model));
            LOG.debug("Updating Hire Monitoring - total loss (original) = '{}', total loss (model) = '{}'", isTotalLossOriginal, model.isIsTotalLostCheck());
            // If total loss has changed, we also need to update the hire monitoring total loss field
            if (isTotalLossOriginal != model.isIsTotalLostCheck()) {
                Customer customer = claim.getCustomer();
                if (customer.getIsTotalLossOriginal() == null) {
                    customer.setIsTotalLossOriginal(customer.getIsTotalLoss());
                }
                customer.setIsTotalLoss(model.isIsTotalLostCheck());
                claim.setCustomer(customer);
            }
            if (claim.getManagingRepair() != managingRepair) {
                if (claim.getManagingRepairOriginal() == null) {
                    claim.setManagingRepairOriginal(claim.getManagingRepair());
                }
                claim.setManagingRepair(managingRepair);
                claim.setManagingRepairLastModified(new Date());
            }
            
            /*
             * labourCost , labourHour, labourRate is defined here as String to
             * accept null value. Struts is not setting null value for those
             * Bigdecimal fields in model class. see bug#1018 for more details.
             */
            if (this.labourCost.trim().isEmpty()) {
                model.setLabourCost(null);
            }
            if (this.labourHour.trim().isEmpty()) {
                model.setLabourHour(null);
            }
            if (this.labourRate.trim().isEmpty()) {
                model.setLabourRate(null);
            }

            boolean updated = false;
            claim.setHireMonitoringDetail(model);

            if ((repairBookedInDateOriginal == null && model.getRepairBookInDate() != null)
                    || (model.getRepairBookInDate() == null && repairBookedInDateOriginal != null)
                    || (repairBookedInDateOriginal != null && model.getRepairBookInDate() != null 
                            && repairBookedInDateOriginal.compareTo(model.getRepairBookInDate()) != 0)) {
                claimService.checkRepairBookedInDateAnomaly(claim);
                // update model in session before calling super.updateModel as model version
                // may have been increased when anomalous added or removed from claim.
                updated = true;
            }

            if (isTotalLossOriginal != model.isIsTotalLostCheck()) {
                claimService.checkTotalLossAnomaly(claim);
                updated=true;
            }
        
            if (updated) {
                // update model in session before calling super.updateModel as model version
                // may have been increased when anomalous added or removed from claim.
                updateModelInSession(Arrays.asList(claim, model, claim.getCustomer()));
            }

            LOG.debug("HireMonitoringDetail to be updated: claim version={}, hmd version={}", claim.getVersion(), model.getVersion());
            eventGenerator.generate(claim, ActivityEvent.HIRE_MONITORING_UPDATED_EVENT);

            return super.updateModel();
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
    }
    
    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("HireMonitoringDetailAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("HireMonitoringDetailAction validate success");
        } else {
            LOG.debug(" HireMonitoringDetailAction validation is not done as claim is null");
        }
    }
        
    @Override
    String getTabName() {
        return TabAccessibility.TAB_HIRE_MONITORING;
    }
    
    public Customer getCustomer() {
        return claim.getCustomer();
    }
    
    public List getNonProvisionReasons() {
        if (nonProvisionReasons == null) {
            nonProvisionReasons = this.lookupService.getNonProvisionReason();
        }
        
        return nonProvisionReasons;
    }
    
}
