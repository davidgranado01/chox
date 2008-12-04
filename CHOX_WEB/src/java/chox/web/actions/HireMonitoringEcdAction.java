/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.HireMonitoringEcd;
import chox.services.HireMonitoringEcdService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdAction extends BaseModelAction implements ModelDriven<HireMonitoringEcd>, Preparable {

    private HireMonitoringEcd model;
    private HireMonitoringEcdService service;
    
    public void setHireMonitoringEcdService(HireMonitoringEcdService service)
    {
        this.service = service;
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_HIRE_MONITORING;
    }

    public HireMonitoringEcd getModel() {
        return model;
    }

    public void prepare() throws Exception {
        model = new HireMonitoringEcd();
    }

    public String addNewHireMonitoringEcd() {
        try {
            Claim claim = claimService.getClaim(claimId);
            model.setClaim(claim);
            model.setCreatedDate(DateHelper.getCurrentTimeStamp());
            model.setCreatedBy(this.getAuthenticatedUser().getUser().getId());
            model.setLastModifiedDate(DateHelper.getCurrentTimeStamp());
            model.setLastModifiedBy(this.getAuthenticatedUser().getUser().getId());
            this.service.updateObject(model);

            this.actionResult = "";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    public List<String> getReasonTypes() {
        List<String> reasonTypes = new ArrayList<String>();
        reasonTypes.add("Reason 1");
        reasonTypes.add("Reason 2");
        return reasonTypes;

    }
}
