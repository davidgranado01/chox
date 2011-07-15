/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.services.LookupService;
import idas.chox.service.notifications.ClaimAnomalousChecker;
import idas.chox.service.notifications.HireUpdatedNotification;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringDetailAction extends ClaimModelAction<HireMonitoringDetail> {

    private static final Logger LOG = LoggerFactory.getLogger(HireMonitoringDetailAction.class);
    private List nonProvisionReasons;
    private LookupService lookupService;
    private ClaimAnomalousChecker hireMonitoringDetailUpdatedChecker;
    private Boolean isUpdateInsurer;
    private Boolean isTotalLossOriginal;
    private String labourRate;
    private String labourHour;
    private String labourCost;

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

    @Override
    public HireMonitoringDetail loadModel() {

        HireMonitoringDetail hireMonitoringDetail = claim.getHireMonitoringDetail();
        if (hireMonitoringDetail != null) {
            isTotalLossOriginal = hireMonitoringDetail.isIsTotalLostCheck();
            return hireMonitoringDetail;
        }
        isTotalLossOriginal = false;
        return new HireMonitoringDetail();
    }

    @Override
    public String updateModel() {
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
        /*
         *  labourCost , labourHour, labourRate is defined here as String to accept null value. 
         *  Struts is not setting null value for those Bigdecimal fields in model class.
         *  see bug#1018 for more details.
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

        claim.setHireMonitoringDetail(model);
        claim.AddNotifications(hireMonitoringDetailUpdatedChecker.getAnomalousChecks(), hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(claim));

        if (isUpdateInsurer) {
            claim.AddNotification(new HireUpdatedNotification());
        }
        isTotalLossOriginal = model.isIsTotalLostCheck();

        return super.updateModel();

    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_HIRE_MONITORING;
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

    public void setHireMonitoringDetailUpdatedChecker(ClaimAnomalousChecker hireMonitoringDetailUpdatedChecker) {
        this.hireMonitoringDetailUpdatedChecker = hireMonitoringDetailUpdatedChecker;
    }

    public Boolean getIsUpdateInsurer() {
        return isUpdateInsurer;
    }

    public void setIsUpdateInsurer(Boolean isUpdateInsurer) {
        this.isUpdateInsurer = isUpdateInsurer;
    }
}
