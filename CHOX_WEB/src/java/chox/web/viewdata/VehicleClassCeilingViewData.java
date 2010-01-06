package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.VehicleClassCeiling;
import java.math.BigDecimal;

public class VehicleClassCeilingViewData {
    private int id;
    private int vehicleClassId;
    private String vehicleClassName;
    private BigDecimal hireNetCeiling;
    private BigDecimal repairNetCeiling;
    private String createdBy;
    private String createdDate;

    public VehicleClassCeilingViewData(VehicleClassCeiling object) {
        this.id = object.getId();
        this.vehicleClassId = object.getVehicleClass().getId();
        this.vehicleClassName = object.getVehicleClass().getName();
        this.hireNetCeiling = object.getHireNetCeiling();
        this.repairNetCeiling = object.getRepairNetCeiling();
        this.createdBy =object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.GridViewDateFormat.format(object.getCreatedDate());
    }
    
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getHireNetCeiling() {
        return hireNetCeiling;
    }

    public void setHireNetCeiling(BigDecimal hireNetCeiling) {
        this.hireNetCeiling = hireNetCeiling;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getRepairNetCeiling() {
        return repairNetCeiling;
    }

    public void setRepairNetCeiling(BigDecimal repairNetCeiling) {
        this.repairNetCeiling = repairNetCeiling;
    }

    public int getVehicleClassId() {
        return vehicleClassId;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

    public String getVehicleClassName() {
        return vehicleClassName;
    }

    public void setVehicleClassName(String vehicleClassName) {
        this.vehicleClassName = vehicleClassName;
    }



    
}
