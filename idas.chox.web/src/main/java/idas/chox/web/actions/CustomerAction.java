/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.List;
import net.sf.json.JSONObject;

public class CustomerAction extends BaseModelAction implements ModelDriven<Customer>, Preparable {

    private Customer model;
    private LookupService lookupService;
    private VehicleClassService vehicleClassService;
    private InsurerService insurerService;
    private int insurerId;
    private int vehicleClassId;

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public Customer getModel() {
        return model;
    }

    public void prepare() throws Exception {
        Claim claim = getClaim();
        model = claim.getCustomer();
        if (model == null) {
            model = new Customer();
            claim.setCustomer(model);
        }
    }

    public String updateModel() {

        if (vehicleClassId >= 0) {
            model.setVehicleClass(this.vehicleClassService.getVehicleClass(vehicleClassId));
        }

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

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getVehicleClassId() {
        return this.model.getVehicleClass() != null ? vehicleClassId = this.model.getVehicleClass().getId() : 0;
    }

    /* EDITED @ 20081208
    public int getInsurerId()
    {
    return this.model.getInsurer() != null ? insurerId = this.model.getInsurer().getId() : 0;
    }
     */
    public List<VehicleClass> getVehicleClasses() {
        return this.lookupService.getVehicleClasses();
    }

    public List<Insurer> getInsurer() {
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
