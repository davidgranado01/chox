/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.EngineerReport;
import idas.chox.core.services.EngineerReportService;
import idas.chox.web.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class EngineerReportAction extends BaseModelAction implements ModelDriven<EngineerReport>, Preparable {

    private EngineerReportService service;
    private EngineerReport model;

    public void setEngineerReportService(EngineerReportService service) {
        this.service = service;
    }

    public EngineerReport getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new EngineerReport();
        } else {
            model = service.getObject(objectId);
        }
    }

    public String updateModel() {
        try {
            if (objectId <= 0) {
                Claim c = claimService.getClaim(getClaimId());
                c.setEngineerReport(model);
                this.claimService.updateClaim(c);
                this.getActionResponse().AssignNewIdResult(c.getId());
            } else {
                this.service.updateObject(model);
            }
        } catch (Exception ex) {
            this.getActionResponse().AddError(ex.getMessage());
        }
        return SUCCESS;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
}
