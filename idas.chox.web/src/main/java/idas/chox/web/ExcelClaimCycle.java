package idas.chox.web;


/**
 *
 * @author seeni
 */
public class ExcelClaimCycle {

    private String choReference;
    private String modifiedDate;
    private String modifiedBy;
    private String status;
    private String reverted;

    public String getChoReference() {
        return choReference;
    }

    public void setChoReference(String choReference) {
        this.choReference = choReference;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getReverted() {
        return reverted;
    }

    public void setReverted(String reverted) {
        this.reverted = reverted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
