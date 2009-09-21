package chox.model;

import java.io.Serializable;

public class Attachment extends AuditableEntity implements Serializable {

    protected String fileName;
    protected String remarks;
    protected String category;
    protected Claim claim;
    protected String fileType;
    protected byte[] fileBuffer;

    public byte[] getFileBuffer() {
        return fileBuffer;
    }

    public void setFileBuffer(byte[] fileBuffer) {
        this.fileBuffer = fileBuffer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
