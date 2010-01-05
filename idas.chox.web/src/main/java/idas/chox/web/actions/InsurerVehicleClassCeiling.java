package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerVehicleClassCeiling extends BaseAction implements ModelDriven<VehicleClassCeiling>, Preparable {

    private int insurerId;
    private int vehicleClassId;
    private int vehicleClassCeilingId;
    private double hireNetCeiling = 0.00;
    private double repairNetCeiling = 0.00;
    private String objectId;
    private VehicleClassCeiling model;
    private List<VehicleClassCeilingViewData> vehicleClassCeilingViewData = new ArrayList<VehicleClassCeilingViewData>();
    private AdminInsurerService adminInsurerService;

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

    // <editor-fold defaultstate="collapsed" desc="GET SET">
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
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    public String getSelectedInsurerVehicleClassCeiling() {

        try {

            List<VehicleClassCeiling> vehicleClassCeilings = adminInsurerService.getVehicleClassCeilingByInsurer(this.insurerId);

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

            ActionResponse response;
            response = adminInsurerService.addNewVehicleClassCeiling(this.model, this.vehicleClassId, this.insurerId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeVehicleClassCeiling() throws Exception {

        try {

            if (this.vehicleClassCeilingId > 0) {
                ActionResponse response;
                response = adminInsurerService.removeVehicleClassCeiling(this.vehicleClassCeilingId);
                setActionResponse(response);
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String updateVehicleClassCeiling() {

        try {

            ActionResponse response;
            response = adminInsurerService.updateVehicleClassCeiling(this.vehicleClassCeilingId, this.hireNetCeiling, this.repairNetCeiling);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }
    // </editor-fold>
}
