package idas.chox.web.actions;

import idas.chox.core.model.VehicleClass;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.VehicleClassCeilingService;
import idas.chox.core.services.VehicleClassService;
import java.util.ArrayList;
import java.util.List;

public class VehicleClassDropDownAction extends BaseAction {

    private List vehicleClasses = null;
    private Integer orgId;
    private InsurerService insurerService;
    private VehicleClassService vehicleClassService;
    private VehicleClassCeilingService vehicleClassCeilingService;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setVehicleClassCeilingService(VehicleClassCeilingService vehicleClassCeilingService) {
        this.vehicleClassCeilingService = vehicleClassCeilingService;
    }

    public List getVehicleClasses() {
        return vehicleClasses;
    }

    @Override
    public String execute() throws Exception {

        List<VehicleClass> vehicleClassesList = vehicleClassService.getAllVehicleClass();
        List<VehicleClassCeiling> vehicleClassCeilings = vehicleClassCeilingService.getVehicleClassCeilingByInsurer(insurerService.getInsurer(orgId));
        List<VehicleClass> newVehicleClasses = new ArrayList<VehicleClass>();

        for (VehicleClass v : vehicleClassesList) {
            if (!isVehicleClassCeilingExist(vehicleClassCeilings, v.getId())) {
                newVehicleClasses.add(v);
            }
        }

        vehicleClasses = newVehicleClasses;
        return SUCCESS;
    }

    private boolean isVehicleClassCeilingExist(List<VehicleClassCeiling> vehicleClassCeilings, int vehicleClassId) {

        boolean bFlag = false;

        for (VehicleClassCeiling vc : vehicleClassCeilings) {

            if (vc.getVehicleClass().getId() == vehicleClassId) {
                bFlag = true;
                break;
            }

        }

        return bFlag;

    }
}
