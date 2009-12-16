package chox.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import scsbre.model.IInsurerInfo;
import chox.Util.TextHelper;

public class Insurer extends AuditableEntity implements Serializable, IInsurerInfo {

    protected String name;
    protected BigDecimal adminHandlingCharge;
    protected BigDecimal choAgreedBenefitValue;
    protected BigDecimal scsAgreedBenefitShareValue;
    protected boolean status;
    protected String address1;
    protected String address2;
    protected String address3;
    protected String address4;
    protected String address5;
    protected String postcode;
    protected String vatNo;
    protected String companyNo;
    protected String phone;
    protected boolean workgroupEnable;
    protected boolean autoRoutingEnable;
    protected boolean claimOwnershipEnable;
    protected boolean claimLocked;
    protected List<VehicleClassCeiling> vehicleClassCeilings;

    public Insurer() {
        vehicleClassCeilings = new ArrayList<VehicleClassCeiling>();
    }

    public BigDecimal getChoAgreedBenefitValue() {
        return choAgreedBenefitValue;
    }

    public void setChoAgreedBenefitValue(BigDecimal choAgreedBenefitValue) {
        this.choAgreedBenefitValue = choAgreedBenefitValue;
    }

    public BigDecimal getScsAgreedBenefitShareValue() {
        return scsAgreedBenefitShareValue;
    }

    public void setScsAgreedBenefitShareValue(BigDecimal scsAgreedBenefitShareValue) {
        this.scsAgreedBenefitShareValue = scsAgreedBenefitShareValue;
    }

    public BigDecimal getAdminHandlingCharge() {
        return adminHandlingCharge;
    }

    public void setAdminHandlingCharge(BigDecimal adminHandlingCharge) {
        this.adminHandlingCharge = adminHandlingCharge;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getVatNo() {
        return vatNo;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    public boolean isWorkgroupEnable() {
        return workgroupEnable;
    }

    public void setWorkgroupEnable(boolean workgroupEnable) {
        this.workgroupEnable = workgroupEnable;
    }
    
    public List<VehicleClassCeiling> getVehicleClassCeilings() {
        return vehicleClassCeilings;
    }

    public void setVehicleClassCeilings(List<VehicleClassCeiling> vehicleClassCeilings) {
        this.vehicleClassCeilings = vehicleClassCeilings;
    }

    public void AddVehicleClassCeiling(VehicleClassCeiling vehicleClassCeiling)
    {
        if(!this.vehicleClassCeilings.contains(vehicleClassCeiling))
        {
            this.vehicleClassCeilings.add(vehicleClassCeiling);
        }
    }

    public boolean isAutoRoutingEnable() {
        return autoRoutingEnable;
    }

    public void setAutoRoutingEnable(boolean autoRoutingEnable) {
        this.autoRoutingEnable = autoRoutingEnable;
    }

    public boolean isClaimOwnershipEnable() {
        return claimOwnershipEnable;
    }

    public void setClaimOwnershipEnable(boolean claimOwnershipEnable) {
        this.claimOwnershipEnable = claimOwnershipEnable;
    }

    public boolean isClaimLocked() {
        return claimLocked;
    }

    public void setClaimLocked(boolean claimLocked) {
        this.claimLocked = claimLocked;
    }

    public String getDisplayAddress() {

        String strDelimiter = ", ";
        StringBuffer sb = new StringBuffer();

        if (TextHelper.isValidText(this.address1)) {
            if (TextHelper.isValidText(sb.toString())) {
                sb.append(strDelimiter);
            }
            sb.append(this.address1);
        }

        if (TextHelper.isValidText(this.address2)) {
            if (TextHelper.isValidText(sb.toString())) {
                sb.append(strDelimiter);
            }
            sb.append(this.address2);
        }

        if (TextHelper.isValidText(this.address3)) {
            if (TextHelper.isValidText(sb.toString())) {
                sb.append(strDelimiter);
            }
            sb.append(this.address3);
        }
        
        if (TextHelper.isValidText(this.postcode)) {
            if (TextHelper.isValidText(sb.toString())) {
                sb.append(strDelimiter);
            }
            sb.append(this.postcode);
        }


        if (TextHelper.isValidText(sb.toString())) {
            return sb.toString();
        } else {
            return "N/A";
        }
    }
}
