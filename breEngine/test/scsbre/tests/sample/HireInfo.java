package scsbre.tests.sample;

import java.util.Date;

import scsbre.model.IHireInfo;
import scsbre.model.IVehicleClassInfo;

public class HireInfo implements IHireInfo {

    protected boolean IsTotalLoss;
    protected String vehicleRegistration;
    protected String vehicleManufacturer;
    protected String vehicleModel;
    protected Date rentalStart;
    protected Date rentalEnd;
    protected String collectionReason;
    protected Integer days;
    protected boolean cdwFee;
    protected boolean automaticFee;
    protected boolean satNavFee;
    protected boolean estateFee;
    protected boolean babySeatFee;
    protected boolean towBarsFee;
    protected boolean nonStandardInsurancePremiumFee;
    protected boolean adminFee;
    protected boolean roofRackFee;
    protected boolean dualControlFee;
    protected boolean deliveryCollectionFee;
    protected VehicleClassInfo vehicleClass;

    public void setIsTotalLoss(boolean IsTotalLoss) {
        this.IsTotalLoss = IsTotalLoss;
    }

    public java.lang.String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(java.lang.String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }

    public java.lang.String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public void setVehicleManufacturer(java.lang.String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    public java.lang.String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(java.lang.String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public java.util.Date getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(java.util.Date rentalStart) {
        this.rentalStart = rentalStart;
    }

    public java.util.Date getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(java.util.Date rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public java.lang.String getCollectionReason() {
        return collectionReason;
    }

    public void setCollectionReason(java.lang.String collectionReason) {
        this.collectionReason = collectionReason;
    }

    public int getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public boolean isCdwFee() {
        return cdwFee;
    }

    public void setCdwFee(boolean cdwFee) {
        this.cdwFee = cdwFee;
    }

    public boolean isAutomaticFee() {
        return automaticFee;
    }

    public void setAutomaticFee(boolean automaticFee) {
        this.automaticFee = automaticFee;
    }

    public boolean isSatNavFee() {
        return satNavFee;
    }

    public void setSatNavFee(boolean satNavFee) {
        this.satNavFee = satNavFee;
    }

    public boolean isEstateFee() {
        return estateFee;
    }

    public void setEstateFee(boolean estateFee) {
        this.estateFee = estateFee;
    }

    public boolean isBabySeatFee() {
        return babySeatFee;
    }

    public void setBabySeatFee(boolean babySeatFee) {
        this.babySeatFee = babySeatFee;
    }

    public boolean isTowBarsFee() {
        return towBarsFee;
    }

    public void setTowBarsFee(boolean towBarsFee) {
        this.towBarsFee = towBarsFee;
    }

    public boolean isNonStandardInsurancePremiumFee() {
        return nonStandardInsurancePremiumFee;
    }

    public void setNonStandardInsurancePremiumFee(boolean nonStandardInsurancePremiumFee) {
        this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
    }

    public boolean isAdminFee() {
        return adminFee;
    }

    public void setAdminFee(boolean adminFee) {
        this.adminFee = adminFee;
    }

    public boolean isRoofRackFee() {
        return roofRackFee;
    }

    public void setRoofRackFee(boolean roofRackFee) {
        this.roofRackFee = roofRackFee;
    }

    public boolean isDualControlFee() {
        return dualControlFee;
    }

    public void setDualControlFee(boolean dualControlFee) {
        this.dualControlFee = dualControlFee;
    }

    public boolean isDeliveryCollectionFee() {
        return deliveryCollectionFee;
    }

    public void setDeliveryCollectionFee(boolean deliveryCollectionFee) {
        this.deliveryCollectionFee = deliveryCollectionFee;
    }

    public VehicleClassInfo getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClassInfo vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public VehicleClassInfo getVClass() {
        return this.vehicleClass;
    }

    public boolean getIsTotalLoss() {
        return this.IsTotalLoss;
    }
}
