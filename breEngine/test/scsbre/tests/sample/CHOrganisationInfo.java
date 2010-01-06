package scsbre.tests.sample;

import scsbre.model.ICHOrganisationInfo;

public class CHOrganisationInfo implements ICHOrganisationInfo {

    protected String name;
    protected String address1;
    protected String address2;
    protected String address3;
    protected String address4;
    protected String address5;
    protected String postcode;
    protected String vatNo;
    protected String companyNo;
    protected boolean delegatedAuthority;
    protected boolean status;
    protected String phone;

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getAddress1() {
        return address1;
    }

    public void setAddress1(java.lang.String address1) {
        this.address1 = address1;
    }

    public java.lang.String getAddress2() {
        return address2;
    }

    public void setAddress2(java.lang.String address2) {
        this.address2 = address2;
    }

    public java.lang.String getAddress3() {
        return address3;
    }

    public void setAddress3(java.lang.String address3) {
        this.address3 = address3;
    }

    public java.lang.String getAddress4() {
        return address4;
    }

    public void setAddress4(java.lang.String address4) {
        this.address4 = address4;
    }

    public java.lang.String getAddress5() {
        return address5;
    }

    public void setAddress5(java.lang.String address5) {
        this.address5 = address5;
    }

    public java.lang.String getPostcode() {
        return postcode;
    }

    public void setPostcode(java.lang.String postcode) {
        this.postcode = postcode;
    }

    public java.lang.String getVatNo() {
        return vatNo;
    }

    public void setVatNo(java.lang.String vatNo) {
        this.vatNo = vatNo;
    }

    public java.lang.String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(java.lang.String companyNo) {
        this.companyNo = companyNo;
    }

    public boolean isDelegatedAuthority() {
        return delegatedAuthority;
    }

    public void setDelegatedAuthority(boolean delegatedAuthority) {
        this.delegatedAuthority = delegatedAuthority;
    }
    
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
}
