package idas.chox.core.model;

/**
 *
 * @author seeni
 */

import java.io.Serializable;

public class UploadedXMLClaimsDetail extends Entity implements Serializable {

    private String choReference;
    private String claimStatus;
    private String processStatus;
    private String remark;
    private String message;
    private String breFailureMessages;
    private int bordereauId;
    private Integer claimId;
    private boolean valid;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public int getBordereauId() {
        return bordereauId;
    }

    public void setBordereauId(int bordereauId) {
        this.bordereauId = bordereauId;
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

    public String getChoReference() {
        return choReference;
    }

    public void setChoReference(String supplierReferenceNumber) {
        this.choReference = supplierReferenceNumber;
    }

    public String getBreFailureMessages() {
        return breFailureMessages;
    }

    public void setBreFailureMessages(String breFailureMessages) {
        this.breFailureMessages = breFailureMessages;
    }

}
