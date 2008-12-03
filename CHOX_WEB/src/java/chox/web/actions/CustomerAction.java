/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Customer;
import chox.services.CustomerService;
import chox.services.InsurerService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;
import chox.services.LookupService;
import chox.services.VehicleClassService;
import chox.model.VehicleClass;
import chox.model.Insurer;
import java.util.List;
/**
 *
 * @author Emmanuel
 */
public class CustomerAction extends BaseModelAction implements ModelDriven<Customer>, Preparable {

    private CustomerService service;
    private Customer model;
    private LookupService lookupService;
    private VehicleClassService vehicleClassService;
    private InsurerService insurerService;
    private int insurerId;
    private int vehicleClassId;

    public void setLookupService( LookupService lookupService)
    {
        this.lookupService = lookupService;
    }
    
    public void setVehicleClassService(VehicleClassService vehicleClassService)
    {
        this.vehicleClassService = vehicleClassService;
    }
    
    public void setInsurerService(InsurerService insurerService)
    {
        this.insurerService = insurerService;
    }
    
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
        
        if(vehicleClassId >= 0)
        {
           model.setVehicleClass(this.vehicleClassService.getObject(vehicleClassId));
        }

        if(insurerId >= 0)
        {
           model.setInsurer(this.insurerService.getObject(insurerId));
        }
        
        try {
            this.service.updateObject(model);
            this.actionResult = "";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    public void setVehicleClassId(int vehicleClassId)
    {
        this.vehicleClassId = vehicleClassId;
    }
    
    public void setInsurerId(int insurerId)
    {
        this.insurerId = insurerId;
    }
    
    public int getVehicleClassId()
    {
        return this.model.getVehicleClass() != null ? vehicleClassId = this.model.getVehicleClass().getId() : 0;
    }
    
    public int getInsurerId()
    {
        return this.model.getInsurer() != null ? insurerId = this.model.getInsurer().getId() : 0;
    }
    
    public List<VehicleClass> getVehicleClasses()
    {
        return this.lookupService.getVehicleClasses();
    }
    
    public List<Insurer> getInsurer()
    {
        return this.lookupService.getInsurers();
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
