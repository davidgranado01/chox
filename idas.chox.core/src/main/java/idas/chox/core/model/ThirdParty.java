package idas.chox.core.model;

import java.io.Serializable;

public class ThirdParty extends Entity implements Serializable {

    /**
     * This attribute maps to the column policy_number in the third_party table.
     */
    protected String policyNumber;
    /**
     * This attribute maps to the column claim_reference in the third_party table.
     */
    protected String claimReference;
    /**
     * This attribute maps to the column vehicle_registration in the third_party table.
     */
    protected String vehicleRegistration;
    /**
     * This attribute maps to the column vehicle_manufacturer in the third_party table.
     */
    protected String vehicleManufacturer;
    /**
     * This attribute maps to the column vehicle_model in the third_party table.
     */
    protected String vehicleModel;
    /**
     * This attribute maps to the column first_name in the third_party table.
     */
    protected String firstName;
    /**
     * This attribute maps to the column address1 in the third_party table.
     */
    protected String address1;
    /**
     * This attribute maps to the column address2 in the third_party table.
     */
    protected String address2;
    /**
     * This attribute maps to the column address3 in the third_party table.
     */
    protected String address3;
    /**
     * This attribute maps to the column address4 in the third_party table.
     */
    protected String address4;
    /**
     * This attribute maps to the column address5 in the third_party table.
     */
    protected String address5;
    /**
     * This attribute maps to the column postcode in the third_party table.
     */
    protected String postcode;
    /**
     * This attribute maps to the column telephone_day in the third_party table.
     */
    protected String telephoneDay;
    /**
     * This attribute maps to the column telephone_evening in the third_party table.
     */
    protected String telephoneEvening;
    /**
     * This attribute maps to the column email in the third_party table.
     */
    protected String email;
    /**
     * This attribute maps to the column last_name in the third_party table.
     */
    protected String lastName;
    /**
     * This attribute maps to the column title in the third_party table.
     */
    protected String title;
    /**
     * This attribute represents the foreign key relationship to the insurer table.
     */
    protected Insurer insurer;
    /**
     * This attribute represents the foreign key relationship to the vehicle_class table.
     */
    protected VehicleClass vehicleClass;

    /**
     * Method 'ThirdParty'
     *
     */
    public ThirdParty() {
    }

    /**
     * Method 'getPolicyNumber'
     *
     * @return java.lang.String
     */
    public java.lang.String getPolicyNumber() {
        return policyNumber;
    }

    /**
     * Method 'setPolicyNumber'
     *
     * @param policyNumber
     */
    public void setPolicyNumber(java.lang.String policyNumber) {
        this.policyNumber = policyNumber;
    }

    /**
     * Method 'getClaimReference'
     *
     * @return java.lang.String
     */
    public java.lang.String getClaimReference() {
        return claimReference;
    }

    /**
     * Method 'setClaimReference'
     *
     * @param claimReference
     */
    public void setClaimReference(java.lang.String claimReference) {
        this.claimReference = claimReference;
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
     * Method 'getFirstName'
     *
     * @return java.lang.String
     */
    public java.lang.String getFirstName() {
        return firstName;
    }

    /**
     * Method 'setFirstName'
     *
     * @param firstName
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }

    /**
     * Method 'getAddress1'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress1() {
        return address1;
    }

    /**
     * Method 'setAddress1'
     *
     * @param address1
     */
    public void setAddress1(java.lang.String address1) {
        this.address1 = address1;
    }

    /**
     * Method 'getAddress2'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress2() {
        return address2;
    }

    /**
     * Method 'setAddress2'
     *
     * @param address2
     */
    public void setAddress2(java.lang.String address2) {
        this.address2 = address2;
    }

    /**
     * Method 'getAddress3'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress3() {
        return address3;
    }

    /**
     * Method 'setAddress3'
     *
     * @param address3
     */
    public void setAddress3(java.lang.String address3) {
        this.address3 = address3;
    }

    /**
     * Method 'getAddress4'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress4() {
        return address4;
    }

    /**
     * Method 'setAddress4'
     *
     * @param address4
     */
    public void setAddress4(java.lang.String address4) {
        this.address4 = address4;
    }

    /**
     * Method 'getAddress5'
     *
     * @return java.lang.String
     */
    public java.lang.String getAddress5() {
        return address5;
    }

    /**
     * Method 'setAddress5'
     *
     * @param address5
     */
    public void setAddress5(java.lang.String address5) {
        this.address5 = address5;
    }

    /**
     * Method 'getPostcode'
     *
     * @return java.lang.String
     */
    public java.lang.String getPostcode() {
        return postcode;
    }

    /**
     * Method 'setPostcode'
     *
     * @param postcode
     */
    public void setPostcode(java.lang.String postcode) {
        this.postcode = postcode;
    }

    /**
     * Method 'getTelephoneDay'
     *
     * @return java.lang.String
     */
    public java.lang.String getTelephoneDay() {
        return telephoneDay;
    }

    /**
     * Method 'setTelephoneDay'
     *
     * @param telephoneDay
     */
    public void setTelephoneDay(java.lang.String telephoneDay) {
        this.telephoneDay = telephoneDay;
    }

    /**
     * Method 'getTelephoneEvening'
     *
     * @return java.lang.String
     */
    public java.lang.String getTelephoneEvening() {
        return telephoneEvening;
    }

    /**
     * Method 'setTelephoneEvening'
     *
     * @param telephoneEvening
     */
    public void setTelephoneEvening(java.lang.String telephoneEvening) {
        this.telephoneEvening = telephoneEvening;
    }

    /**
     * Method 'getEmail'
     *
     * @return java.lang.String
     */
    public java.lang.String getEmail() {
        return email;
    }

    /**
     * Method 'setEmail'
     *
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    /**
     * Method 'getLastName'
     *
     * @return java.lang.String
     */
    public java.lang.String getLastName() {
        return lastName;
    }

    /**
     * Method 'setLastName'
     *
     * @param lastName
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }

    /**
     * Method 'getTitle'
     *
     * @return java.lang.String
     */
    public java.lang.String getTitle() {
        return title;
    }

    /**
     * Method 'setTitle'
     *
     * @param title
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    /**
     * Method 'getInsurer'
     *
     * @return Insurer
     */
    public Insurer getInsurer() {
        return insurer;
    }

    /**
     * Method 'setInsurer'
     *
     * @param insurer
     */
    public void setInsurer(Insurer insurer) {
        this.insurer = insurer;
    }

    /**
     * Method 'getVehicleClass'
     *
     * @return VehicleClass
     */
    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    /**
     * Method 'setVehicleClass'
     *
     * @param vehicleClass
     */
    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
}
