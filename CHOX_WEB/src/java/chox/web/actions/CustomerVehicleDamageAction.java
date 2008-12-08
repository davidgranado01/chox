package chox.web.actions;

import chox.model.Claim;
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
public class CustomerVehicleDamageAction extends BaseModelAction implements ModelDriven<Customer>, Preparable {

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
            if(model.getId() > 0)
            {
                this.service.updateObject(model);
            }
            else
            {
                Claim c = claimService.getClaim(getClaimId());
                c.setCustomer(model);
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
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }    
    
}

