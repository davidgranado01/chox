package idas.chox.core.model;

import java.io.Serializable;

public class Bordereau extends Entity implements Serializable {

    private byte[] fileBuffer;
    private String fileName;
    private String status;
    private String description;
    private String message;
    private Integer totalClaims;
    private Long fileSize;
    private boolean processed;
    private boolean beingProcessed;
    private boolean valid;

    public boolean isBeingProcessed() {
        return beingProcessed;
    }

    public void setBeingProcessed(boolean beingProcessed) {
        this.beingProcessed = beingProcessed;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getTotalClaims() {
        return totalClaims;
    }

    public void setTotalClaims(Integer totalClaims) {
        this.totalClaims = totalClaims;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
