package idas.chox.core.model;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Customer extends Entity implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(Customer.class);

    private Boolean isVehicleRegistrationexist = false;
    /**
     * This attribute maps to the column title in the customer table.
     */
    private String title;
    /**
     * This attribute maps to the column first_name in the customer table.
     */
    private String firstName;
    /**
     * This attribute maps to the column last_name in the customer table.
     */
    private String lastName;
    /**
     * This attribute maps to the column address1 in the customer table.
     */
    private String address1;
    /**
     * This attribute maps to the column address2 in the customer table.
     */
    private String address2;
    /**
     * This attribute maps to the column address3 in the customer table.
     */
    private String address3;
    /**
     * This attribute maps to the column address4 in the customer table.
     */
    private String address4;
    /**
     * This attribute maps to the column address5 in the customer table.
     */
    private String address5;
    /**
     * This attribute maps to the column postcode in the customer table.
     */
    private String postcode;
    /**
     * This attribute maps to the column telephone_day in the customer table.
     */
    private String telephoneDay;
    /**
     * This attribute maps to the column telephone_evening in the customer table.
     */
    private String telephoneEvening;
    /**
     * This attribute maps to the column email in the customer table.
     */
    private String email;
    /**
     * This attribute maps to the column vehicle_registration in the customer table.
     */
    private String vehicleRegistration;
    /**
     * This attribute maps to the column vehicle_manufacturer in the customer table.
     */
    private String vehicleManufacturer;
    /**
     * This attribute maps to the column vehicle_model in the customer table.
     */
    private String vehicleModel;
    /**
     * This attribute maps to the column vehicle_year in the customer table.
     */
    private String vehicleYear;

    /**
     * This attribute maps to the column location in the customer table.
     */
    private String location;
    /**
     * This attribute maps to the column damage in the customer table.
     */
    private String damage;
    /**
     * This attribute maps to the column initial_ecd in the customer table.
     */
    private Date initialECD;
    /**
     * This attribute maps to the column policy_number in the customer table.
     */
    private String policyNumber;
    /**
     * This attribute maps to the column claim_reference in the customer table.
     */
    private String claimReference;
    /**
     * This attribute maps to the column is_primary_driver in the customer table.
     */
    private boolean isPrimaryDriver;
    /**
     * This attribute maps to the column is_usable in the customer table.
     */
    private Boolean isUsable;
    
    /**
     * This attribute maps to the column is_active in the customer table.
     */
    private boolean isActive;
    /**
     * This attribute maps to the column comprehensive in the customer table.
     */
    private boolean comprehensive;
    /**
     * This attribute maps to the column insurer_id in the customer table.
     */
    /*
    protected Insurer insurer;
     */
    /**
     * This attribute represents the foreign key relationship to the vehicle_class table.
     */
    private VehicleClass vehicleClass;
    private Boolean isTotalLoss;
    private Boolean isTotalLossOriginal;
    private String insurerName;
    private Integer age;
    private String occupation;
    private String policyUsage;

    /**
     * Mitigation attributes
     */
    private Boolean canAccessOtherVehicle;
    private Boolean otherVehicleUsed;
    private String otherVehicle;
    private Boolean courtesyCarEntitled;
    private Boolean specificVehicleRequired;
    private String specificVehicleReason;
    private String typeVehicleRequired;
    private String specialRequirements;
    private String averageDailyMileage;

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
    private Claim claim;

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public Customer() {
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
        if (this.vehicleRegistration != null) {
            this.vehicleRegistration = this.vehicleRegistration.replaceAll(" ", "");
        }
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

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    /**
     * Method 'getLocation'
     *
     * @return java.lang.String
     */
    public java.lang.String getLocation() {
        return location;
    }

    /**
     * Method 'setLocation'
     *
     * @param location
     */
    public void setLocation(java.lang.String location) {
        this.location = location;
    }

    /**
     * Method 'getDamage'
     *
     * @return java.lang.String
     */
    public java.lang.String getDamage() {
        return damage;
    }

    /**
     * Method 'setDamage'
     *
     * @param damage
     */
    public void setDamage(java.lang.String damage) {
        this.damage = damage;
    }

    /**
     * Method 'getInitialEcd'
     *
     * @return java.util.Date
     */
    public java.util.Date getInitialECD() {
        return initialECD;
    }

    public String getInitialECDDesc() {
        String result = "Not Supplied";
        if (initialECD != null) {
            Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            result = dateFormat.format(initialECD);
        }
        return result;
    }

    /**
     * Method 'setInitialEcd'
     *
     * @param initialEcd
     */
    public void setInitialECD(Date initialEcd) {
        this.initialECD = initialEcd;
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
     * Method 'isIsPrimaryDriver'
     *
     * @return boolean
     */
    public boolean isIsPrimaryDriver() {
        return isPrimaryDriver;
    }

    /**
     * Method 'setIsPrimaryDriver'
     *
     * @param isPrimaryDriver
     */
    public void setIsPrimaryDriver(boolean isPrimaryDriver) {
        this.isPrimaryDriver = isPrimaryDriver;
    }

    /**
     * Method 'isIsUsable'
     *
     * @return boolean
     */
    public Boolean getIsUsable() {
        return isUsable;
    }
    
    public void setIsUsableDesc(String isUsable) {
        if ("yes".equalsIgnoreCase(isUsable)) {
            this.isUsable = Boolean.TRUE;
        } else if ("no".equalsIgnoreCase(isUsable)) {
            this.isUsable = Boolean.FALSE;
        } else {
            this.isUsable = null;
        }
    }
    
    public String getIsUsableDesc() {

        return this.isUsable == null ? "" : this.isUsable ? "Yes" : "No";
    }


    /**
     * Method 'setIsUsable'
     *
     * @param isUsable
     */
    public void setIsUsable(Boolean isUsable) {
        this.isUsable = isUsable;
    }

    /**
     * Method 'isIsActive'
     *
     * @return boolean
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * Method 'setIsActive'
     *
     * @param isActive
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Method 'isComprehensive'
     *
     * @return boolean
     */
    public boolean isComprehensive() {
        return comprehensive;
    }

    /**
     * Method 'setComprehensive'
     *
     * @param comprehensive
     */
    public void setComprehensive(boolean comprehensive) {
        this.comprehensive = comprehensive;
    }
    /*
    public Insurer getInsurer() {
    return insurer;
    }

    public void setInsurer(Insurer insurer) {
    this.insurer = insurer;
    }
     */

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

    //convenince wrapppers
    public String getIsComprehensiveDesc() {
        return this.isComprehensive() ? "Yes" : "No";
    }

    public String getFormattedName() {

        String formattedName = "";
        if (title != null && title.length() > 0) {
            formattedName += title + " ";
        }
        if (firstName != null && firstName.length() > 0) {
            formattedName += firstName + " ";
        }
        if (lastName != null && lastName.length() > 0) {
            formattedName += lastName;
        }
        return formattedName;
    }

    public Boolean getIsTotalLoss() {
        return isTotalLoss;
    }

    public void setIsTotalLoss(Boolean isTotalLoss) {
        this.isTotalLoss = isTotalLoss;
    }

    public String getIsTotalLossDesc() {
        if (isTotalLoss == null) {
            return "";
        }
        return this.isTotalLoss ? "Yes" : "No";
    }

    public void setIsVehicleRegistrationExist(Boolean b) {
        isVehicleRegistrationexist = b;
    }

    public Boolean isVehicleRegistrationExist() {
        return isVehicleRegistrationexist;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPolicyUsage() {
        return policyUsage;
    }

    public void setPolicyUsage(String policyUsage) {
        this.policyUsage = policyUsage;
    }

    public String getAverageDailyMileage() {
//        if (otherVehicle == null)
//            return new Integer(0);
//        else
            return averageDailyMileage;
    }

    public void setAverageDailyMileage(String averageDailyMileage) {
        this.averageDailyMileage = averageDailyMileage;
    }

    public Boolean getCanAccessOtherVehicle() {
        return canAccessOtherVehicle;
    }
    public String getCanAccessOtherVehicleDesc() {
        if (canAccessOtherVehicle == null) {
            return "";
        }
        else {
            return canAccessOtherVehicle ? "Yes" : "No";
        }
    }

    public void setCanAccessOtherVehicle(Boolean canAccessOtherVehicle) {
        this.canAccessOtherVehicle = canAccessOtherVehicle;
    }

    public Boolean getCourtesyCarEntitled() {
        return courtesyCarEntitled;
    }
    public String getCourtesyCarEntitledDesc() {
        if (courtesyCarEntitled == null) {
            return "";
        }
        else {
            return courtesyCarEntitled ? "Yes" : "No";
        }
    }

    public void setCourtesyCarEntitled(Boolean courtesyCarEntitled) {
        this.courtesyCarEntitled = courtesyCarEntitled;
    }

    public String getOtherVehicle() {
            return otherVehicle;
    }

    public void setOtherVehicle(String otherVehicle) {
        this.otherVehicle = otherVehicle;
    }

    public Boolean getOtherVehicleUsed() {
        return otherVehicleUsed;
    }
    public String getOtherVehicleUsedDesc() {
        if (otherVehicleUsed == null) {
            return "";
        }
        else {
            return otherVehicleUsed ? "Yes" : "No";
        }
    }

    public void setOtherVehicleUsed(Boolean otherVehicleUsed) {
        this.otherVehicleUsed = otherVehicleUsed;
    }

    public String getSpecialRequirements() {
            return specialRequirements;
    }

    public void setSpecialRequirements(String specialRequirements) {
        this.specialRequirements = specialRequirements;
    }

    public String getSpecificVehicleReason() {
            return specificVehicleReason;
    }

    public void setSpecificVehicleReason(String specificVehicleReason) {
        this.specificVehicleReason = specificVehicleReason;
    }

    public Boolean getSpecificVehicleRequired() {
        return specificVehicleRequired;
    }
    public String getSpecificVehicleRequiredDesc() {
        if (specificVehicleRequired == null) {
            return "";
        }
        else {
            return specificVehicleRequired ? "Yes" : "No";
        }
    }

    public void setSpecificVehicleRequired(Boolean specificVehicleRequired) {
        this.specificVehicleRequired = specificVehicleRequired;
    }

    public String getTypeVehicleRequired() {
            return typeVehicleRequired;
    }

    public void setTypeVehicleRequired(String typeVehicleRequired) {
        this.typeVehicleRequired = typeVehicleRequired;
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

    public Boolean getIsTotalLossOriginal() {
        return isTotalLossOriginal;
    }

    public String getIsTotalLossOriginalDesc() {
        if (isTotalLossOriginal == null) {
            return "";
        }
        else {
            return isTotalLossOriginal ? "(Yes)" : "(No)";
        }
    }

    public void setIsTotalLossOriginal(Boolean isTotalLossOriginal) {
        this.isTotalLossOriginal = isTotalLossOriginal;
    }
}

