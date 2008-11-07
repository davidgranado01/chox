/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import com.filesystemsoftware.utils.StringEncoder;

public class Withness {
    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long claimID = -1;
    private String name;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String postcode;
    private String telephoneDay;
    private String telephoneEvening;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    
}
