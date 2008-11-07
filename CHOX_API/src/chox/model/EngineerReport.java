/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import java.math.BigDecimal;
/**
 *
 * @author Carlson
 */
public class EngineerReport {

    private long ID = -1;
    private long claimID = -1;
    private BigDecimal labourAmount;
    private BigDecimal totalAmount;
    private BigDecimal days;
    private String usable;
    private String name;
    private String company;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String postcode;
    private String telephone;
    private String email;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

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

    public long getClaimID() {
        return claimID;
    }

    public void setClaimID(long claimID) {
        this.claimID = claimID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getLabourAmount() {
        return labourAmount;
    }

    public void setLabourAmount(BigDecimal labourAmount) {
        this.labourAmount = labourAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }
    
    
}
