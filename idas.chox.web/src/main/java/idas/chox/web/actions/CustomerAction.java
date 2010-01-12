/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Customer;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.List;

public class CustomerAction extends ClaimModelAction<Customer> {

    private LookupService lookupService;
    private VehicleClassService vehicleClassService;
    private int vehicleClassId;

    @Override
    protected Customer loadModel() {

        if (claim.getCustomer() == null) {
            return new Customer();
        } else {
            return claim.getCustomer();
        }
    }

    @Override
    public String updateModel() {

        claim.setCustomer(model);
        if (vehicleClassId >= 0) {
            claim.getCustomer().setVehicleClass(this.vehicleClassService.getVehicleClass(vehicleClassId));
        }
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public int getVehicleClassId() {
        Customer customer = getModel();
        return customer.getVehicleClass() != null ? vehicleClassId = customer.getVehicleClass().getId() : 0;
    }

    public List<VehicleClass> getVehicleClasses() {
        return this.lookupService.getVehicleClasses();
    }

    public List<Insurer> getInsurer() {
        return this.lookupService.getInsurers();
    }
}
