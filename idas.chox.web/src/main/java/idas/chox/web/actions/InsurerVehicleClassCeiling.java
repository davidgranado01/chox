package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.VehicleClassCeilingService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;
import java.math.BigDecimal;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerVehicleClassCeiling extends BaseAction implements ModelDriven<VehicleClassCeiling>, Preparable {

    private String objectId;
    private VehicleClassCeiling model;
    protected int insurerId;
    protected int vehicleClassId;
    protected int vehicleClassCeilingId;
    protected double hireNetCeiling = 0.00;
    protected double repairNetCeiling = 0.00;
    protected List<VehicleClassCeilingViewData> vehicleClassCeilingViewData = new ArrayList<VehicleClassCeilingViewData>();
    protected VehicleClassCeilingService vehicleClassCeilingService;
    protected VehicleClassService vehicleClassService;
    protected InsurerService insurerService;

    public int getVehicleClassCeilingId() {
        return vehicleClassCeilingId;
    }

    public void setVehicleClassCeilingId(int vehicleClassCeilingId) {
        this.vehicleClassCeilingId = vehicleClassCeilingId;
    }

    public int getVehicleClassId() {
        return vehicleClassId;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public double getHireNetCeiling() {
        return hireNetCeiling;
    }

    public void setHireNetCeiling(double hireNetCeiling) {
        this.hireNetCeiling = hireNetCeiling;
    }

    public double getRepairNetCeiling() {
        return repairNetCeiling;
    }

    public void setRepairNetCeiling(double repairNetCeiling) {
        this.repairNetCeiling = repairNetCeiling;
    }

    public boolean getIsNew() {

        if (objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }

    public VehicleClassCeiling getModel() {
        return model;
    }

    public void setModel(VehicleClassCeiling model) {
        this.model = model;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public void prepare() throws Exception {
        model = new VehicleClassCeiling();
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.vehicleClassCeilingViewData);
        return "{totalCount:" + this.vehicleClassCeilingViewData.size() + ",results:" + jObject.toString() + "}";
    }

    public String getSelectedInsurerVehicleClassCeiling() {

        try {

            List<VehicleClassCeiling> vehicleClassCeilings = vehicleClassCeilingService.getSelectedVehicleClassCeilingByInsurer(this.insurerId);

            for (VehicleClassCeiling vcc : vehicleClassCeilings) {
                vehicleClassCeilingViewData.add(new VehicleClassCeilingViewData(vcc));
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String addNewVehicleClassCeiling() {

        try {

            model.setVehicleClass(vehicleClassService.getVehicleClass(this.vehicleClassId));
            model.setInsurer(insurerService.getInsurer(this.insurerId));
            vehicleClassCeilingService.saveVehicleClassCeiling(model);
            getActionResponse().AssignNewIdResult(model.getId());

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeVehicleClassCeiling() throws Exception {

        try {

            if (this.vehicleClassCeilingId > 0) {

                VehicleClassCeiling vehicleClassCeiling = vehicleClassCeilingService.getVehicleClassCeiling(this.vehicleClassCeilingId);
                vehicleClassCeilingService.deleteVehicleClassCeiling(vehicleClassCeiling);

            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String updateVehicleClassCeiling() {
        
        try {

            VehicleClassCeiling vehicleClassCeiling = vehicleClassCeilingService.getVehicleClassCeiling(this.vehicleClassCeilingId);
            vehicleClassCeiling.setHireNetCeiling(new BigDecimal(this.hireNetCeiling));
            vehicleClassCeiling.setRepairNetCeiling(new BigDecimal(this.repairNetCeiling));
            vehicleClassCeilingService.saveVehicleClassCeiling(vehicleClassCeiling);
            
        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }
        
        return SUCCESS;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setVehicleClassCeilingService(VehicleClassCeilingService vehicleClassCeilingService) {
        this.vehicleClassCeilingService = vehicleClassCeilingService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }
}
