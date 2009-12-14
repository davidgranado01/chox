package idas.chox.core.model;

import java.io.Serializable;

public class Bordereau extends AuditableEntity implements Serializable {

    protected byte[] fileBuffer;
    protected String fileName;
    protected String status;
    protected String description;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFileBuffer() {
        return fileBuffer;
    }

    public void setFileBuffer(byte[] fileBuffer) {
        this.fileBuffer = fileBuffer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
