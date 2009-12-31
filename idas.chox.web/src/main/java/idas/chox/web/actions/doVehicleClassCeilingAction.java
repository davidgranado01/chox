/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.VehicleClassCeilingService;
import idas.chox.core.services.VehicleClassService;
import java.math.BigDecimal;

public class doVehicleClassCeilingAction extends BaseAction implements ModelDriven<VehicleClassCeiling>, Preparable {

    private String selectOrgId;
    private VehicleClassCeiling model;
    private Integer vehicleClassId;
    private VehicleClassService vehicleClassService;
    private InsurerService insurerService;
    private VehicleClassCeilingService vehicleClassCeilingService;
    // EDIT
    private BigDecimal hireNetCeiling;
    private BigDecimal repairNetCeiling;
    private String vehicleClassCeilingId;

    public BigDecimal getHireNetCeiling() {
        return hireNetCeiling;
    }

    public void setHireNetCeiling(BigDecimal hireNetCeiling) {
        this.hireNetCeiling = hireNetCeiling;
    }

    public BigDecimal getRepairNetCeiling() {
        return repairNetCeiling;
    }

    public void setRepairNetCeiling(BigDecimal repairNetCeiling) {
        this.repairNetCeiling = repairNetCeiling;
    }

    public String getVehicleClassCeilingId() {
        return vehicleClassCeilingId;
    }

    public void setVehicleClassCeilingId(String vehicleClassCeilingId) {
        this.vehicleClassCeilingId = vehicleClassCeilingId;
    }

    public void setVehicleClassCeilingService(VehicleClassCeilingService vehicleClassCeilingService) {
        this.vehicleClassCeilingService = vehicleClassCeilingService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public VehicleClassCeiling getModel() {
        return model;
    }

    public void setModel(VehicleClassCeiling model) {
        this.model = model;
    }

    public String getSelectOrgId() {
        return selectOrgId;
    }

    public void setSelectOrgId(String selectOrgId) {
        this.selectOrgId = selectOrgId;
    }

    public Integer getVehicleClassId() {
        return vehicleClassId;
    }

    public void setVehicleClassId(Integer vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public String updateVehicleClassCeiling() {

        VehicleClassCeiling vehicleClassCeilingObj = vehicleClassCeilingService.getObject(Integer.valueOf(vehicleClassCeilingId));
        vehicleClassCeilingObj.setHireNetCeiling(hireNetCeiling);
        vehicleClassCeilingObj.setRepairNetCeiling(repairNetCeiling);
        vehicleClassCeilingService.updateObject(vehicleClassCeilingObj);

        return SUCCESS;
    }

    public String updateModel() throws Exception {

        try {

            model.setVehicleClass(vehicleClassService.getObject(vehicleClassId));
            model.setInsurer(insurerService.getInsurer(Integer.parseInt(selectOrgId)));
            vehicleClassCeilingService.updateObject(model);

        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }

    public void prepare() throws Exception {
        model = new VehicleClassCeiling();
    }

    public String removeObject() throws Exception {

        try {

            if (!vehicleClassCeilingId.equalsIgnoreCase("")) {

                VehicleClassCeiling vehicleClassCeiling = vehicleClassCeilingService.getObject(Integer.parseInt(vehicleClassCeilingId));
                vehicleClassCeilingService.deleteObject(vehicleClassCeiling);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            this.getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }
}
