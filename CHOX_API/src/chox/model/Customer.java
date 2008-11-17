package chox.model;

import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable
{
	protected int id;
	protected String title;
	protected String firstnames;
	protected String lastname;
	protected String address1;
	protected String address2;
	protected String address3;
	protected String address4;
	protected String address5;
	protected String postcode;
	protected String telephoneDay;
	protected String telephoneEvening;
	protected String email;
	protected String vehicleRegistration;
	protected String vehicleManufacturer;
	protected String vehicleModel;
	protected String location;
	protected String damage;
	protected Date initialEcd;
	protected String insurerName;
	protected String policyNumber;
	protected String claimReference;
	protected Boolean comprehensive;
	protected int createdBy;
	protected boolean createdByNull = true;
	protected Date createdDate;
	protected int lastModifiedBy;
	protected boolean lastModifiedByNull = true;
	protected Date lastModifiedDate;
	protected Boolean isPrimaryDriver;
	protected boolean isUsable;
	protected Boolean isActive;
	protected VehicleClass vehicleClass;

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress3() {
            return address3;
        }

        public void setAddress3(String address3) {
            this.address3 = address3;
        }

        public String getAddress4() {
            return address4;
        }

        public void setAddress4(String address4) {
            this.address4 = address4;
        }

        public String getAddress5() {
            return address5;
        }

        public void setAddress5(String address5) {
            this.address5 = address5;
        }

        public String getClaimReference() {
            return claimReference;
        }

        public void setClaimReference(String claimReference) {
            this.claimReference = claimReference;
        }

        public Boolean getComprehensive() {
            return comprehensive;
        }

        public void setComprehensive(Boolean comprehensive) {
            this.comprehensive = comprehensive;
        }

        public int getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(int createdBy) {
            this.createdBy = createdBy;
        }

        public boolean isCreatedByNull() {
            return createdByNull;
        }

        public void setCreatedByNull(boolean createdByNull) {
            this.createdByNull = createdByNull;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        public String getDamage() {
            return damage;
        }

        public void setDamage(String damage) {
            this.damage = damage;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstnames() {
            return firstnames;
        }

        public void setFirstnames(String firstnames) {
            this.firstnames = firstnames;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Date getInitialEcd() {
            return initialEcd;
        }

        public void setInitialEcd(Date initialEcd) {
            this.initialEcd = initialEcd;
        }

        public String getInsurerName() {
            return insurerName;
        }

        public void setInsurerName(String insurerName) {
            this.insurerName = insurerName;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Boolean getIsPrimaryDriver() {
            return isPrimaryDriver;
        }

        public void setIsPrimaryDriver(Boolean isPrimaryDriver) {
            this.isPrimaryDriver = isPrimaryDriver;
        }

        public boolean isIsUsable() {
            return isUsable;
        }

        public void setIsUsable(boolean isUsable) {
            this.isUsable = isUsable;
        }

        public int getLastModifiedBy() {
            return lastModifiedBy;
        }

        public void setLastModifiedBy(int lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
        }

        public boolean isLastModifiedByNull() {
            return lastModifiedByNull;
        }

        public void setLastModifiedByNull(boolean lastModifiedByNull) {
            this.lastModifiedByNull = lastModifiedByNull;
        }

        public Date getLastModifiedDate() {
            return lastModifiedDate;
        }

        public void setLastModifiedDate(Date lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getPolicyNumber() {
            return policyNumber;
        }

        public void setPolicyNumber(String policyNumber) {
            this.policyNumber = policyNumber;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getTelephoneDay() {
            return telephoneDay;
        }

        public void setTelephoneDay(String telephoneDay) {
            this.telephoneDay = telephoneDay;
        }

        public String getTelephoneEvening() {
            return telephoneEvening;
        }

        public void setTelephoneEvening(String telephoneEvening) {
            this.telephoneEvening = telephoneEvening;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
