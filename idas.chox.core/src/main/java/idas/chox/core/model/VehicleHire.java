package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

public class VehicleHire extends Entity implements Serializable {

    protected boolean IsTotalLoss;
    protected String vehicleRegistration;
    protected String vehicleManufacturer;
    protected String vehicleModel;
    protected Date rentalStart;
    private Date rentalStart_original;
    protected Date rentalEnd;
    private Date rentalEnd_original;
    protected String collectionReason;
    protected int days;
    private Integer days_original;
    protected boolean miscellaneousFee;
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
    protected VehicleClass vehicleClass;
    private VehicleClass vehicleClass_original;
    /**
     * HPI attributes
     */
    protected String hpiVehicleManufacturer;
    protected String hpiVehicleModel;
    protected String hpiVehicleYear;
    protected String hpiVehicleCapacity;
    protected String hpiVehicleDoorplan;
    protected String hpiVehicleTransmission;
    protected String hpiError;
    protected Date hpiFirstRegistration;
    protected boolean courtesyCarProvided;

    public VehicleHire() {
    }

    public VehicleClass getVehicleClass_original() {
        return vehicleClass_original;
    }

    public void setVehicleClass_original(VehicleClass vehicleClass_original) {
        this.vehicleClass_original = vehicleClass_original;
    }

    public Integer getDays_original() {
        return days_original;
    }

    public void setDays_original(Integer days_original) {
        this.days_original = days_original;
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

    public java.util.Date getRentalStart_original() {
        return rentalStart_original;
    }

    /**
     * Method 'setRentalStart'
     *
     * @param rentalStart
     */
    public void setRentalStart(java.util.Date rentalStart) {
        this.rentalStart = rentalStart;
    }

    public void setRentalStart_original(java.util.Date rentalStart) {
        this.rentalStart_original = rentalStart;
    }

    /**
     * Method 'getRentalEnd'
     *
     * @return java.util.Date
     */
    public java.util.Date getRentalEnd() {
        return rentalEnd;
    }

    public java.util.Date getRentalEnd_original() {
        return rentalEnd_original;
    }

    /**
     * Method 'setRentalEnd'
     *
     * @param rentalEnd
     */
    public void setRentalEnd(java.util.Date rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public void setRentalEnd_original(java.util.Date rentalEnd) {
        this.rentalEnd_original = rentalEnd;
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

    public boolean isMiscellaneousFee() {
        return miscellaneousFee;
    }

    public void setMiscellaneousFee(boolean miscellaneousFee) {
        this.miscellaneousFee = miscellaneousFee;
    }

    /**
     * Method 'isAutomaticFee'
     *
     * @return boolean
     */
    public boolean isAutomaticFee() {
        return automaticFee;
    }

    /**
     * Method 'setAutomaticFee'
     *
     * @param automaticFee
     */
    public void setAutomaticFee(boolean automaticFee) {
        this.automaticFee = automaticFee;
    }

    /**
     * Method 'isSatNavFee'
     *
     * @return boolean
     */
    public boolean isSatNavFee() {
        return satNavFee;
    }

    /**
     * Method 'setSatNavFee'
     *
     * @param satNavFee
     */
    public void setSatNavFee(boolean satNavFee) {
        this.satNavFee = satNavFee;
    }

    /**
     * Method 'isEstateFee'
     *
     * @return boolean
     */
    public boolean isEstateFee() {
        return estateFee;
    }

    /**
     * Method 'setEstateFee'
     *
     * @param estateFee
     */
    public void setEstateFee(boolean estateFee) {
        this.estateFee = estateFee;
    }

    /**
     * Method 'isBabySeatFee'
     *
     * @return boolean
     */
    public boolean isBabySeatFee() {
        return babySeatFee;
    }

    /**
     * Method 'setBabySeatFee'
     *
     * @param babySeatFee
     */
    public void setBabySeatFee(boolean babySeatFee) {
        this.babySeatFee = babySeatFee;
    }

    /**
     * Method 'isTowBarsFee'
     *
     * @return boolean
     */
    public boolean isTowBarsFee() {
        return towBarsFee;
    }

    /**
     * Method 'setTowBarsFee'
     *
     * @param towBarsFee
     */
    public void setTowBarsFee(boolean towBarsFee) {
        this.towBarsFee = towBarsFee;
    }

    public boolean isNonStandardInsurancePremiumFee() {
        return nonStandardInsurancePremiumFee;
    }

    /**
     * Method 'setNonStandardInsurancePremiumFee'
     *
     * @param nonStandardInsurancePremiumFee
     */
    public void setNonStandardInsurancePremiumFee(boolean nonStandardInsurancePremiumFee) {
        this.nonStandardInsurancePremiumFee = nonStandardInsurancePremiumFee;
    }

    /**
     * Method 'isAdminFee'
     *
     * @return boolean
     */
    public boolean isAdminFee() {
        return adminFee;
    }

    /**
     * Method 'setAdminFee'
     *
     * @param adminFee
     */
    public void setAdminFee(boolean adminFee) {
        this.adminFee = adminFee;
    }

    /**
     * Method 'isRoofRackFee'
     *
     * @return boolean
     */
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

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }

    public java.util.Date getHireStart() {
        return this.rentalStart;
    }

    public java.util.Date getHireStart_original() {
        return this.rentalStart_original;
    }

    public void setHireStart(Date hireStart) {
        this.rentalStart = hireStart;
    }

    public void setHireStart_original(Date hireStart) {
        this.rentalStart_original = hireStart;
    }

    public java.util.Date getHireEnd() {
        return this.rentalEnd;
    }

    public java.util.Date getHireEnd_original() {
        return this.rentalEnd_original;
    }

    public void setHireEnd(Date hireEnd) {
        this.rentalEnd = hireEnd;
    }

    public void setHireEnd_original(Date hireEnd) {
        this.rentalEnd_original = hireEnd;
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
