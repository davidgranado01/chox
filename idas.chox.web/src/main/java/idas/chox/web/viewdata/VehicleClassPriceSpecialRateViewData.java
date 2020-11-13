package idas.chox.web.viewdata;

import java.math.BigDecimal;
import java.util.Date;

import idas.chox.core.model.*;
import idas.chox.core.util.DateHelper;

public class VehicleClassPriceSpecialRateViewData {

    private int id;
    private String vehicleClassName;
    private String chorganisationName;
    private String insurerName;
    private BigDecimal rate;
    private String startDate;
    private String createdBy;
    private String createdDate;
    private boolean removed;

    public VehicleClassPriceSpecialRateViewData(VehicleClassPriceSpecialRate object) {
        this.id = object.getId();
        this.vehicleClassName = object.getVehicleClass().getName();
        this.chorganisationName = object.getChorganisation().getName();
        this.insurerName = object.getInsurer().getName();
        this.rate = object.getPrice();
        this.startDate = DateHelper.getLocalDateTimeFormat().format(object.getStartDate());
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.createdBy = object.getCreatedBy().getDisplayName();
    }

    public VehicleClassPriceSpecialRateViewData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public String getChorganisationName() {
        return chorganisationName;
    }

    public void setChorganisationName(String chorganisationName) {
        this.chorganisationName = chorganisationName;
    }

    public String getVehicleClassName() {
        return vehicleClassName;
    }

    public void setVehicleClassName(String vehicleClassName) {
        this.vehicleClassName = vehicleClassName;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

}
