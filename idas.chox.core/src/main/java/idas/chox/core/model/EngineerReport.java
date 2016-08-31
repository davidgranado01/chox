package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class EngineerReport extends Entity implements Serializable {

    /**
     * This attribute maps to the column days in the engineer_report table.
     */
    private Integer days;
    /**
     * This attribute maps to the column name in the engineer_report table.
     */
    private String name;
    /**
     * This attribute maps to the column company in the engineer_report table.
     */
    private String company;
    /**
     * This attribute maps to the column address1 in the engineer_report table.
     */
    private String address1;
    /**
     * This attribute maps to the column address2 in the engineer_report table.
     */
    private String address2;
    /**
     * This attribute maps to the column address3 in the engineer_report table.
     */
    private String address3;
    /**
     * This attribute maps to the column address4 in the engineer_report table.
     */
    private String address4;
    /**
     * This attribute maps to the column address5 in the engineer_report table.
     */
    private String address5;
    /**
     * This attribute maps to the column postcode in the engineer_report table.
     */
    private String postcode;
    /**
     * This attribute maps to the column telephone in the engineer_report table.
     */
    private String telephone;
    /**
     * This attribute maps to the column email in the engineer_report table.
     */
    private String email;
    /**
     * This attribute maps to the column is_usable in the engineer_report table.
     */
    private Boolean isUsable;
    /**
     * This attribute maps to the column labour_amount in the engineer_report table.
     */
    private BigDecimal labourAmount;
    /**
     * This attribute maps to the column total_amount in the engineer_report table.
     */
    private BigDecimal totalAmount;

    /**
     * Method 'EngineerReport'
     *
     */
    public EngineerReport() {
    }

    /**
     * Method 'getDays'
     *
     * @return java.lang.Integer
     */
    public java.lang.Integer getDays() {
        return days;
    }

    /**
     * Method 'setDays'
     *
     * @param days
     */
    public void setDays(java.lang.Integer days) {
        this.days = days;
    }

    /**
     * Method 'getName'
     *
     * @return java.lang.String
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Method 'setName'
     *
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Method 'getCompany'
     *
     * @return java.lang.String
     */
    public java.lang.String getCompany() {
        return company;
    }

    /**
     * Method 'setCompany'
     *
     * @param company
     */
    public void setCompany(java.lang.String company) {
        this.company = company;
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
     * Method 'getTelephone'
     *
     * @return java.lang.String
     */
    public java.lang.String getTelephone() {
        return telephone;
    }

    /**
     * Method 'setTelephone'
     *
     * @param telephone
     */
    public void setTelephone(java.lang.String telephone) {
        this.telephone = telephone;
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
     * Method 'isIsUsable'
     *
     * @return boolean
     */
    public Boolean isIsUsable() {
        return isUsable;
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
     * Method 'getLabourAmount'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getLabourAmount() {
        return labourAmount;
    }

    /**
     * Method 'setLabourAmount'
     *
     * @param labourAmount
     */
    public void setLabourAmount(java.math.BigDecimal labourAmount) {
        this.labourAmount = labourAmount;
    }

    /**
     * Method 'getTotalAmount'
     *
     * @return java.math.BigDecimal
     */
    public java.math.BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    /**
     * Method 'setTotalAmount'
     *
     * @param totalAmount
     */
    public void setTotalAmount(java.math.BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getEstimatedLabourAmount() {
        return this.labourAmount;
    }

    public BigDecimal getEstimatedTotalRepairAmount() {
        return this.totalAmount;
    }

    public int getEstimatedDaysUnderRepair() {
        if (this.days == null)
            return 0;

        return this.days;
    }

    public String getIsUsableDesc() {
        if (isUsable == null)
            return "Unknown";
        
        return isUsable ? "Yes" : "No";

    }
    
    public enum DisplayName {
        // Insert in the order as they are displayed in the UI. 
        LABOUR_AMOUNT       ("labourAmount", "Estimated Labour Amount"),
        TOTAL_AMOUNT        ("totalAmount", "Estimated Total Repair Amount"),
        ESTIMATED_DAYS      ("days", "Estimated Days Under Repair"),
        IS_USABLE           ("isUsable", "Usable?"),
        NAME                ("name", "Name"),
        COMPANY             ("company", "Company"),
        ADDRESS1            ("address1", "Engineer Address 1"),
        ADDRESS2            ("address2", "Engineer Address 2"),
        ADDRESS3            ("address3", "Engineer Address 3"),
        ADDRESS4            ("address4", "Engineer Address 4"),
        ADDRESS5            ("address5", "Engineer Address 5"),
        POSTCODE            ("postcode", "Engineer Postcode"),
        TELEPHONE           ("telephone", "Engineer Telephone"),
        EMAIL               ("email", "Engineer Email");
        
        private final String parameterName;
        private final String displayName;

        DisplayName(String name, String displayName) {
            this.parameterName = name;
            this.displayName = displayName;
        }

        public String getParameterName() {
            return parameterName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
