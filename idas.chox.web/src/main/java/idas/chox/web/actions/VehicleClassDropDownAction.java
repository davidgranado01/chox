package idas.chox.web.actions;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.services.VehicleClassCeilingService;
import java.util.ArrayList;
import java.util.List;

public class VehicleClassDropDownAction extends BaseAction {

    protected List vehicleClasses = new ArrayList<VehicleClass>();
    protected int insurerId;
    protected VehicleClassCeilingService vehicleClassCeilingService;

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setVehicleClassCeilingService(VehicleClassCeilingService vehicleClassCeilingService) {
        this.vehicleClassCeilingService = vehicleClassCeilingService;
    }

    public List getVehicleClasses() {
        return vehicleClasses;
    }

    @Override
    public String execute() throws Exception {
        
        try {
            
            vehicleClasses = vehicleClassCeilingService.getAvailableVehicleClassCeilingByInsurer(this.insurerId);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }
        return SUCCESS;
    }

}
