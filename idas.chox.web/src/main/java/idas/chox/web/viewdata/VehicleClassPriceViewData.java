package idas.chox.web.viewdata;

import idas.chox.core.model.VehicleClassPrice;
import idas.chox.core.util.DateHelper;

import java.math.BigDecimal;

public class VehicleClassPriceViewData {

    private int id;
    private String vehicleClassName;
    private BigDecimal rate;
    private String startDate;
    private String createdBy;
    private String createdDate;
    private int age;
    private boolean showDeleteLink;


    public VehicleClassPriceViewData(VehicleClassPrice object, boolean showDelete) {
        this.id = object.getId();
        this.vehicleClassName = object.getVehicleClass().getName();
        this.rate = object.getPrice();
        this.startDate = DateHelper.getLocalDateTimeFormat().format(object.getStartDate());
        this.createdBy = object.getCreatedBy().getDisplayName();
        this.createdDate = DateHelper.getLocalDateTimeFormat().format(object.getCreatedDate());
        this.age = object.getAge().intValue();
        this.showDeleteLink = showDelete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean getShowDeleteLink() {
        return showDeleteLink;
    }

    public void setShowDeleteLink(boolean showDeleteLink) {
        this.showDeleteLink = showDeleteLink;
    }

}
