package idas.chox.core.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import idas.chox.core.util.TextHelper;

public class Insurer extends Entity implements Serializable {

    private String name;
    private BigDecimal adminHandlingCharge;
    private BigDecimal choAgreedBenefitValue;
    private BigDecimal scsAgreedBenefitShareValue;
    private BigDecimal fixedTransactionalFeeValue;
    private boolean status;
    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String address5;
    private String postcode;
    private String vatNo;
    private String companyNo;
    private String phone;
    private String supportProcedure;
    private boolean fixedTransactionalFee;
    private boolean workgroupEnable;
    private boolean fnolEnable;
    private boolean engineersEnable;
    private boolean autoRoutingEnable;
    private boolean claimOwnershipEnable;
    private boolean claimLocked;
    private boolean onlineSupportEnable;
    private boolean taskManagementEnable;
    private List<VehicleClassCeiling> vehicleClassCeilings;
    private Insurer relatedInsurer;
    private Workgroup tpiWorkgroup;
    private WebUser tpiClaimOwner;
    private String tpiRegexExpression;

    public String getTpiRegexExpression() {
        return tpiRegexExpression;
    }

    public void setTpiRegexExpression(String tpiRegexExpression) {
        this.tpiRegexExpression = tpiRegexExpression;
    }


     public Workgroup getTpiWorkgroup() {
         return tpiWorkgroup;
    }

    public WebUser getTpiClaimOwner() {
        return tpiClaimOwner;
    }

    public void setTpiWorkgroup(Workgroup workgroup) {
        this.tpiWorkgroup=workgroup;
    }

    public void setTpiClaimOwner(WebUser webUser) {
        this.tpiClaimOwner=webUser;
    }

    public Insurer getRelatedInsurer() {
        return relatedInsurer;
    }

    public void setRelatedInsurer(Insurer relatedInsurer) {
        this.relatedInsurer = relatedInsurer;
    }

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

    public BigDecimal getFixedTransactionalFeeValue() {
        return fixedTransactionalFeeValue;
    }

    public void setFixedTransactionalFeeValue(BigDecimal fixedTransactionalFeeValue) {
        this.fixedTransactionalFeeValue = fixedTransactionalFeeValue;
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

    public boolean isFnolEnable() {
        return fnolEnable;
    }

    public void setFnolEnable(boolean fnolEnable) {
        this.fnolEnable = fnolEnable;
    }

    public boolean isEngineersEnable() {
        return engineersEnable;
    }

    public void setEngineersEnable(boolean engineersEnable) {
        this.engineersEnable = engineersEnable;
    }

    public boolean isWorkgroupEnable() {
        return workgroupEnable;
    }

    public void setWorkgroupEnable(boolean workgroupEnable) {
        this.workgroupEnable = workgroupEnable;
    }

    public boolean isFixedTransactionalFee() {
        return fixedTransactionalFee;
    }

    public boolean getFixedTransactionalFee() {
        return fixedTransactionalFee;
    }

    public void setFixedTransactionalFee(boolean fixedTransactionalFee) {
        this.fixedTransactionalFee = fixedTransactionalFee;
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

    public boolean isOnlineSupportEnable() {
        return onlineSupportEnable;
    }

    public void setOnlineSupportEnable(boolean onlineSupportEnable) {
        this.onlineSupportEnable = onlineSupportEnable;
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

    public boolean isTaskManagementEnable() {
        return taskManagementEnable;
    }

    public void setTaskManagementEnable(boolean taskManagementEnable) {
        this.taskManagementEnable = taskManagementEnable;
    }

    public void setClaimLocked(boolean claimLocked) {
        this.claimLocked = claimLocked;
    }

    public String getSupportProcedure() {
        return supportProcedure;
    }

    public void setSupportProcedure(String supportProcedure) {
        this.supportProcedure = supportProcedure;
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
