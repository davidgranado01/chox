package idas.chox.web.viewdata;

import java.math.BigDecimal;

import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.util.DateHelper;

public class VehicleClassCeilingViewData {

    private int id;
    private int vehicleClassId;
    private String vehicleClassName;
    private BigDecimal hireNetCeiling;
    private BigDecimal repairNetCeiling;
    private String createdBy;
    private String createdDate;
    private boolean removed;

    public VehicleClassCeilingViewData(VehicleClassCeiling object) {
        this.id = object.getId();
        this.vehicleClassId = object.getVehicleClass().getId();
        this.vehicleClassName = object.getVehicleClass().getName();
        this.hireNetCeiling = object.getHireNetCeiling();
        this.repairNetCeiling = object.getRepairNetCeiling();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
    }
    
    public VehicleClassCeilingViewData(ProtocolVehicleClassCeiling object) {
        this.id = object.getId();
        this.vehicleClassId = object.getVehicleClass().getId();
        this.vehicleClassName = object.getVehicleClass().getName();
        this.hireNetCeiling = object.getHireNetCeiling();
        this.repairNetCeiling = object.getRepairNetCeiling();
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
    }

    public VehicleClassCeilingViewData() {
    }
    
    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
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
