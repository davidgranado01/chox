/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.EngineerReport;
import idas.chox.service.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class EngineerReportAction extends BaseModelAction implements ModelDriven<EngineerReport>, Preparable {

    private EngineerReport model;

    public EngineerReport getModel() {
        return model;
    }

    public void prepare() throws Exception {
        Claim claim = getClaim();
        model = claim.getEngineerReport();

        if (model == null) {
            model = new EngineerReport();
            claim.setEngineerReport(model);
        }
    }

    public String updateModel() {
        try {
            boolean isTransient = model.isTransient();
            Claim claim = getClaim();
            this.claimService.updateClaim(claim);
            if (isTransient) {
                this.getActionResponse().AssignNewIdResult(model.getId());
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
