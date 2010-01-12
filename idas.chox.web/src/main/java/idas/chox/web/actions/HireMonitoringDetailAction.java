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

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringDetailAction extends ClaimModelAction<HireMonitoringDetail> {

    private List nonProvisionReasons;
    private LookupService lookupService;
    private ClaimAnomalousChecker hireMonitoringDetailUpdatedChecker;
    private Boolean isUpdateInsurer;

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    @Override
    public HireMonitoringDetail loadModel() {

        HireMonitoringDetail hireMonitoringDetail = claim.getHireMonitoringDetail();
        if (hireMonitoringDetail != null) {
            return hireMonitoringDetail;
        }
        return new HireMonitoringDetail();
    }

    @Override
    public String updateModel() {

        claim.setHireMonitoringDetail(model);
        claim.AddNotifications(hireMonitoringDetailUpdatedChecker.getAnomalousChecks(), hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(claim));

        if (isUpdateInsurer) {
            claim.AddNotification(new HireUpdatedNotification());
        }
        return super.updateModel();

    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_HIRE_MONITORING;
    }

    public Customer getCustomer()
    {
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
