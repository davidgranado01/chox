package idas.chox.web.actions;

import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.VehicleClassCeilingService;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class VehicleClassCeilingAction extends BaseAction {

    private List<VehicleClassCeilingViewData> vehicleClassCeilingViewData;
    private InsurerService insurerService;
    private VehicleClassCeilingService vehicleClassCeilingService;
    private int insurerId = -1;

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.vehicleClassCeilingViewData);
        return "{totalCount:" + this.vehicleClassCeilingViewData.size() + ",results:" + jObject.toString() + "}";
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setVehicleClassCeilingService(VehicleClassCeilingService vehicleClassCeilingService) {
        this.vehicleClassCeilingService = vehicleClassCeilingService;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getSelectedInsurerVehicleClassCeiling() {

        try {

            List<VehicleClassCeiling> vehicleClassCeilings = vehicleClassCeilingService.getVehicleClassCeilingByInsurer(insurerService.getObject(insurerId));
            vehicleClassCeilingViewData = new ArrayList<VehicleClassCeilingViewData>();

            for (VehicleClassCeiling vcc : vehicleClassCeilings) {
                vehicleClassCeilingViewData.add(new VehicleClassCeilingViewData(vcc));
            }

        } catch (Exception ex) {
            logger.error(ex);
            getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }
}
