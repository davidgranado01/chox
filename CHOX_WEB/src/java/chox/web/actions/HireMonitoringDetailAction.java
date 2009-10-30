/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.Customer;
import chox.model.HireMonitoringDetail;
import chox.model.notifications.ClaimAnomalousChecker;
import chox.model.notifications.HireUpdatedNotification;
import chox.services.CustomerService;
import chox.services.HireMonitoringDetailService;
import chox.services.LookupService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.List;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringDetailAction extends BaseModelAction implements ModelDriven<HireMonitoringDetail>, Preparable {

    private HireMonitoringDetailService service;
    private CustomerService customerService;
    private HireMonitoringDetail model;
    private Customer customer;
    private int customerId;
    private List nonProvisionReasons;
    private LookupService lookupService;
    private ClaimAnomalousChecker hireMonitoringDetailUpdatedChecker;
    private Boolean isUpdateInsurer;

    public void setHireMonitoringDetailService(HireMonitoringDetailService service) {
        this.service = service;
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public HireMonitoringDetail getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new HireMonitoringDetail();
        } else {
            model = service.getObject(objectId);
        }

        if (customerId <= 0) {
            customer = new Customer();

        } else {
            customer = customerService.getObject(customerId);
        }
    }

    public String updateModel() {
        
        try {

            boolean isNewHireMonitoringDetail = false;
            
            if(model.getId()==null){
                isNewHireMonitoringDetail = true;
            }

            Claim c = claimService.getClaim(getClaimId());
            c.setHireMonitoringDetail(model);
            c.AddNotifications(hireMonitoringDetailUpdatedChecker.getAnomalousChecks(), hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(c));

            if(isUpdateInsurer){
                c.AddNotification(new HireUpdatedNotification());
            }
            
            claimService.updateClaim(c);

            if (isNewHireMonitoringDetail) {
                this.getActionResponse().AssignNewIdResult(model.getId());
            }

        } catch (Exception ex) {
            getActionResponse().AddError(ex.getMessage());
        }
        
        return SUCCESS;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_HIRE_MONITORING;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
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
