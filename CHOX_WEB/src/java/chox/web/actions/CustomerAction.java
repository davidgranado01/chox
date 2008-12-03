/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Customer;
import chox.services.CustomerService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class CustomerAction extends BaseModelAction implements ModelDriven<Customer>, Preparable {

    private CustomerService service;
    private Customer model;

    public void setCustomerService(CustomerService service) {
        this.service = service;
    }

    public Customer getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Customer();
        } else {
            model = service.getObject(objectId);
        }
    }
    
    public String updateModel() {
        try {
            this.service.updateObject(model);
            this.actionResult = "1";
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
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }
   
    
}
