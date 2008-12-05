/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.Invoice;
import chox.services.InvoiceService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class InvoiceAction extends BaseModelAction implements ModelDriven<Invoice>, Preparable {

    private InvoiceService service;
    private Invoice model;
    
    public void setInvoiceService(InvoiceService service) {
        this.service = service;
    }

    public Invoice getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Invoice();
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
                Claim c = claimService.getClaim(getClaimId());
                c.setInvoice(model);
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
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }    
    
}
