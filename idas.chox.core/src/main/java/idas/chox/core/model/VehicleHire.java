package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

public class VehicleHire extends Entity implements Serializable {

    private boolean IsTotalLoss;
    private String vehicleRegistration;
    private String vehicleManufacturer;
    private String vehicleModel;
    private Date rentalStart;
    private Date rentalStartOriginal;
    private Date rentalEnd;
    private Date rentalEndOriginal;
    private String collectionReason;
    private int days;
    private Integer daysOriginal;
//    private boolean acquisitionFee;
//    private boolean overheadFee;
//    private boolean miscellaneousFee;
//    private boolean automaticFee;
//    private boolean satNavFee;
//    private boolean estateFee;
//    private boolean babySeatFee;
//    private boolean towBarsFee;
//    private boolean nonStandardInsurancePremiumFee;
//    private boolean adminFee;
//    private boolean roofRackFee;
//    private boolean dualControlFee;
//    private boolean deliveryCollectionFee;
    private VehicleClass vehicleClass;
    private VehicleClass vehicleClassOriginal;
    /**
     * HPI attributes
     */
    private String hpiVehicleManufacturer;
    private String hpiVehicleModel;
    private String hpiVehicleYear;
    private String hpiVehicleCapacity;
    private String hpiVehicleDoorplan;
    private String hpiVehicleTransmission;
    private String hpiError;
    private Date hpiFirstRegistration;
    private boolean courtesyCarProvided;

    public VehicleHire() {
    }

    public VehicleClass getVehicleClassOriginal() {
        return vehicleClassOriginal;
    }

    public void setVehicleClassOriginal(VehicleClass vehicleClassOriginal) {
        this.vehicleClassOriginal = vehicleClassOriginal;
    }

    public Integer getDaysOriginal() {
        return daysOriginal;
    }

    public void setDaysOriginal(Integer daysOriginal) {
        this.daysOriginal = daysOriginal;
    }

    public void setIsTotalLoss(boolean IsTotalLoss) {
        this.IsTotalLoss = IsTotalLoss;
    }

    /**
     * Method 'getVehicleRegistration'
     *
     * @return java.lang.String
     */
    public java.lang.String getVehicleRegistration() {
        return vehicleRegistration;
    }

    /**
     * Method 'setVehicleRegistration'
     *
     * @param vehicleRegistration
     */
    public void setVehicleRegistration(java.lang.String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
        if (this.vehicleRegistration != null)
            this.vehicleRegistration = this.vehicleRegistration.replaceAll(" ", "");
    }

    /**
     * Method 'getVehicleManufacturer'
     *
     * @return java.lang.String
     */
    public java.lang.String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    /**
     * Method 'setVehicleManufacturer'
     *
     * @param vehicleManufacturer
     */
    public void setVehicleManufacturer(java.lang.String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    /**
     * Method 'getVehicleModel'
     *
     * @return java.lang.String
     */
    public java.lang.String getVehicleModel() {
        return vehicleModel;
    }

    /**
     * Method 'setVehicleModel'
     *
     * @param vehicleModel
     */
    public void setVehicleModel(java.lang.String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    /**
     * Method 'getRentalStart'
     *
     * @return java.util.Date
     */
    public java.util.Date getRentalStart() {
        return rentalStart;
    }

    public java.util.Date getRentalStartOriginal() {
        return rentalStartOriginal;
    }

    /**
     * Method 'setRentalStart'
     *
     * @param rentalStart
     */
    public void setRentalStart(java.util.Date rentalStart) {
        this.rentalStart = rentalStart;
    }

    public void setRentalStartOriginal(java.util.Date rentalStart) {
        this.rentalStartOriginal = rentalStart;
    }

    /**
     * Method 'getRentalEnd'
     *
     * @return java.util.Date
     */
    public java.util.Date getRentalEnd() {
        return rentalEnd;
    }

    public java.util.Date getRentalEndOriginal() {
        return rentalEndOriginal;
    }

    /**
     * Method 'setRentalEnd'
     *
     * @param rentalEnd
     */
    public void setRentalEnd(java.util.Date rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public void setRentalEndOriginal(java.util.Date rentalEnd) {
        this.rentalEndOriginal = rentalEnd;
    }

    /**
     * Method 'getCollectionReason'
     *
     * @return java.lang.String
     */
    public java.lang.String getCollectionReason() {
        return collectionReason;
    }

    /**
     * Method 'setCollectionReason'
     *
     * @param collectionReason
     */
    public void setCollectionReason(java.lang.String collectionReason) {
        this.collectionReason = collectionReason;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
       // days = (days == null) ? 0 : days;
        this.days = days;
    }

/******
    public boolean isMiscellaneousFee() {
        return miscellaneousFee;
    }

    public void setMiscellaneousFee(boolean miscellaneousFee) {
        this.miscellaneousFee = miscellaneousFee;
    }

    public boolean isAcquisitionFee() {
        return acquisitionFee;
    }

    public void setAcquisitionFee(boolean acquisitionFee) {
        this.acquisitionFee = acquisitionFee;
    }

    public boolean isOverheadFee() {
        return overheadFee;
    }

    public void setOverheadFee(boolean overheadFee) {
        this.overheadFee = overheadFee;
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
*****/

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public java.util.Date getHireStart() {
        return this.rentalStart;
    }

    public java.util.Date getHireStartOriginal() {
        return this.rentalStartOriginal;
    }

    public void setHireStart(Date hireStart) {
        this.rentalStart = hireStart;
    }

    public void setHireStartOriginal(Date hireStart) {
        this.rentalStartOriginal = hireStart;
    }

    public java.util.Date getHireEnd() {
        return this.rentalEnd;
    }

    public java.util.Date getHireEndOriginal() {
        return this.rentalEndOriginal;
    }

    public void setHireEnd(Date hireEnd) {
        this.rentalEnd = hireEnd;
    }

    public void setHireEndOriginal(Date hireEnd) {
        this.rentalEndOriginal = hireEnd;
    }

    // ##### NOT FROM HERE #############
    public boolean getIsTotalLoss() {
        return this.IsTotalLoss;
    }

    public String getHpiError() {
        return hpiError;
    }

    public void setHpiError(String hpiError) {
        this.hpiError = hpiError;
    }

    public String getHpiVehicleCapacity() {
        return hpiVehicleCapacity;
    }

    public void setHpiVehicleCapacity(String hpiVehicleCapacity) {
        this.hpiVehicleCapacity = hpiVehicleCapacity;
    }

    public String getHpiVehicleDoorplan() {
        return hpiVehicleDoorplan;
    }

    public void setHpiVehicleDoorplan(String hpiVehicleDoorplan) {
        this.hpiVehicleDoorplan = hpiVehicleDoorplan;
    }

    public String getHpiVehicleManufacturer() {
        return hpiVehicleManufacturer;
    }

    public void setHpiVehicleManufacturer(String hpiVehicleManufacturer) {
        this.hpiVehicleManufacturer = hpiVehicleManufacturer;
    }

    public String getHpiVehicleModel() {
        return hpiVehicleModel;
    }

    public void setHpiVehicleModel(String hpiVehicleModel) {
        this.hpiVehicleModel = hpiVehicleModel;
    }

    public String getHpiVehicleTransmission() {
        return hpiVehicleTransmission;
    }

    public void setHpiVehicleTransmission(String hpiVehicleTransmission) {
        this.hpiVehicleTransmission = hpiVehicleTransmission;
    }

    public String getHpiVehicleYear() {
        return hpiVehicleYear;
    }

    public void setHpiVehicleYear(String hpiVehicleYear) {
        this.hpiVehicleYear = hpiVehicleYear;
    }

    public void setHpiFirstRegistration(Date firstRegistration) {
        this.hpiFirstRegistration = firstRegistration;
    }

    public Date getHpiFirstRegistration() {
        return hpiFirstRegistration;
    }

    public boolean isCourtesyCarProvided() {
        return courtesyCarProvided;
    }

    public String getCourtesyCarProvidedDesc() {
        return (courtesyCarProvided ? "Yes" : "No");
    }

    public void setCourtesyCarProvided(boolean courtesyCarProvided) {
        this.courtesyCarProvided = courtesyCarProvided;
    }
}
