/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.HireMonitoringDetail;
import chox.services.HireMonitoringDetailService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringDetailAction extends BaseModelAction implements ModelDriven<HireMonitoringDetail>, Preparable {

    private HireMonitoringDetailService service;
    private HireMonitoringDetail model;    

    public void setHireMonitoringDetailService(HireMonitoringDetailService service) {
        this.service = service;
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
    }   

    public String updateModel() {
        try {
            if(model.getId() > 0)
            {
                this.service.updateObject(model);
            }
            else
            {
                model.setCreatedDate(DateHelper.getCurrentTimeStamp());
                model.setCreatedBy(this.getAuthenticatedUser().getUser().getId());  
                model.setLastModifiedDate(DateHelper.getCurrentTimeStamp());
                model.setLastModifiedBy(this.getAuthenticatedUser().getUser().getId()); 
                Claim c = claimService.getClaim(getClaimId());
                c.setHireMonitoringDetail(model);
                this.claimService.updateClaim(c);
            }
            this.actionResult = "";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
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
}
