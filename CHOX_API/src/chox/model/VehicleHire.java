package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class VehicleHire implements Serializable
{
	protected int id;
	protected String vehicleRegistration;
	protected String vehicleManufacturer;
	protected String vehicleModel;
	protected Date rentalStart;
	protected Date rentalEnd;
	protected String collectionReason;
	protected long days;
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
	protected int createdBy;
	protected Date createdDate;
	protected int lastModifiedBy;
	protected Date lastModifiedDate;
	protected VehicleClass vehicleClass;

    public boolean isAdminFee() {
        return adminFee;
    }

    public void setAdminFee(boolean adminFee) {
        this.adminFee = adminFee;
    }

    public boolean isAutomaticFee() {
        return automaticFee;
    }

    public void setAutomaticFee(boolean automaticFee) {
        this.automaticFee = automaticFee;
    }

    public boolean isBabySeatFee() {
        return babySeatFee;
    }

    public void setBabySeatFee(boolean babySeatFee) {
        this.babySeatFee = babySeatFee;
    }

    public boolean isCdwFee() {
        return cdwFee;
    }

    public void setCdwFee(boolean cdwFee) {
        this.cdwFee = cdwFee;
    }

    public String getCollectionReason() {
        return collectionReason;
    }

    public void setCollectionReason(String collectionReason) {
        this.collectionReason = collectionReason;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public boolean isDeliveryCollectionFee() {
        return deliveryCollectionFee;
    }

    public void setDeliveryCollectionFee(boolean deliveryCollectionFee) {
        this.deliveryCollectionFee = deliveryCollectionFee;
    }

    public boolean isDualControlFee() {
        return dualControlFee;
    }

    public void setDualControlFee(boolean dualControlFee) {
        this.dualControlFee = dualControlFee;
    }

    public boolean isEstateFee() {
        return estateFee;
    }

    public void setEstateFee(boolean estateFee) {
        this.estateFee = estateFee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean isNonStandardInsurancePremiumFee() {
        return nonStandardInsurancePremiumFee;
    }

    public void setNonStandardInsurancePremiumFee(boolean nonStandardInsurancePremiumFee) {
        this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
    }

    public Date getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(Date rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public Date getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(Date rentalStart) {
        this.rentalStart = rentalStart;
    }

    public boolean isRoofRackFee() {
        return roofRackFee;
    }

    public void setRoofRackFee(boolean roofRackFee) {
        this.roofRackFee = roofRackFee;
    }

    public boolean isSatNavFee() {
        return satNavFee;
    }

    public void setSatNavFee(boolean satNavFee) {
        this.satNavFee = satNavFee;
    }

    public boolean isTowBarsFee() {
        return towBarsFee;
    }

    public void setTowBarsFee(boolean towBarsFee) {
        this.towBarsFee = towBarsFee;
    }

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }


        
}
