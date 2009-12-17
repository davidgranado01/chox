/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.services.LookupService;
import idas.chox.service.notifications.ClaimAnomalousChecker;
import idas.chox.service.notifications.HireUpdatedNotification;
import idas.chox.web.security.ApplicationAccessibility;
import java.util.List;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringDetailAction extends BaseModelAction implements ModelDriven<HireMonitoringDetail>, Preparable {

    private HireMonitoringDetail model;
    private List nonProvisionReasons;
    private LookupService lookupService;
    private ClaimAnomalousChecker hireMonitoringDetailUpdatedChecker;
    private Boolean isUpdateInsurer;

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public HireMonitoringDetail getModel() {
        return model;
    }

    public void prepare() throws Exception {

        Claim claim = getClaim();
        model = claim.getHireMonitoringDetail();
        if (model == null) {
            model = new HireMonitoringDetail();
        }
    }

    public String updateModel() {

        try {

            boolean isTransient = model.isTransient();

            Claim c = claimService.getClaim(getClaimId());
            c.setHireMonitoringDetail(model);
            c.AddNotifications(hireMonitoringDetailUpdatedChecker.getAnomalousChecks(), hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(c));

            if (isUpdateInsurer) {
                c.AddNotification(new HireUpdatedNotification());
            }

            claimService.updateClaim(c);

            if (isTransient) {
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
