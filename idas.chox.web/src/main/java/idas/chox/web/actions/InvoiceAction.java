/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Invoice;
import idas.chox.web.security.ApplicationAccessibility;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class InvoiceAction extends BaseModelAction implements ModelDriven<Invoice>, Preparable {

    private Invoice model;

    public Invoice getModel() {
        return model;
    }

    public void prepare() throws Exception {
        Claim claim = getClaim();
        model = claim.getInvoice();
        if (model == null) {
            model = new Invoice();
            claim.setInvoice(model);
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
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
}
