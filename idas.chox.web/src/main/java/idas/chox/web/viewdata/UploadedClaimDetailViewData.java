package idas.chox.web.viewdata;

import idas.chox.core.model.UploadedXMLClaimsDetail;

public class UploadedClaimDetailViewData {

    private String supplierReferenceNumber;
    private String claimStatus;
    private String processStatus;
    private String remark;
    private String message;
    private String breFailureMessages;
    private int bordereauId;
    private Integer claimId;
    private boolean valid;

    public UploadedClaimDetailViewData(UploadedXMLClaimsDetail data) {

        this.supplierReferenceNumber = data.getChoReference();
        this.claimStatus = data.getClaimStatus();
        this.processStatus = data.getProcessStatus();
        this.remark = data.getRemark();
        this.message = data.getMessage();
        this.bordereauId = data.getBordereauId();
        this.claimId = data.getClaimId();
        this.valid = data.isValid();
        this.breFailureMessages = data.getBreFailureMessages();
    }

    public UploadedClaimDetailViewData() {
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSupplierReferenceNumber() {
        return supplierReferenceNumber;
    }

    public void setSupplierReferenceNumber(String supplierReferenceNumber) {
        this.supplierReferenceNumber = supplierReferenceNumber;
    }

    public int getBordereauId() {
        return bordereauId;
    }

    public void setBordereauId(int bordereauId) {
        this.bordereauId = bordereauId;
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getBreFailureMessages() {
        return breFailureMessages;
    }

    public void setBreFailureMessages(String breFailureMessages) {
        this.breFailureMessages = breFailureMessages;
    }
    
    
}
