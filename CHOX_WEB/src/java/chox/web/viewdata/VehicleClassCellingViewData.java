package chox.web.viewdata;

import chox.Util.DateHelper;
import chox.model.VehicleClassCelling;
import java.math.BigDecimal;

public class VehicleClassCellingViewData {
    private int id;
    private int vehicleClassId;
    private String vehicleClassName;
    private BigDecimal hireNetCelling;
    private BigDecimal repairNetCelling;
    private String createdBy;
    private String createdDate;

    public VehicleClassCellingViewData(VehicleClassCelling object) {
        this.id = object.getId();
        this.vehicleClassId = object.getVehicleClass().getId();
        this.vehicleClassName = object.getVehicleClass().getName();
        this.hireNetCelling = object.getHireNetCelling();
        this.repairNetCelling = object.getRepairNetCelling();
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

    public BigDecimal getHireNetCelling() {
        return hireNetCelling;
    }

    public void setHireNetCelling(BigDecimal hireNetCelling) {
        this.hireNetCelling = hireNetCelling;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getRepairNetCelling() {
        return repairNetCelling;
    }

    public void setRepairNetCelling(BigDecimal repairNetCelling) {
        this.repairNetCelling = repairNetCelling;
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
