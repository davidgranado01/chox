/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.viewdata;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.BordereauWithoutFile;
import java.text.Format;
import java.text.SimpleDateFormat;

/**
 *
 * @author seeni
 */
public class BordereauViewData {

    private int id;
    private String status;
    private String fileName;
    private String createdDate;
    private String createdBy;
    private String description;
    private Integer totalClaims;
    private String message;
    private boolean processed;
    private Long fileSize;
    private boolean valid;
    Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");

    public BordereauViewData(Bordereau bordereau) {

        this.id = bordereau.getId();
        this.status = bordereau.getStatus();
        this.fileName = bordereau.getFileName();
        this.createdDate = dateFormat.format(bordereau.getCreatedDate());
        this.createdBy = bordereau.getCreatedBy().getDisplayName();
        this.description = bordereau.getDescription();
        this.totalClaims = bordereau.getTotalClaims();
        this.fileSize = bordereau.getFileSize();
        this.processed = bordereau.isProcessed();
        this.message = bordereau.getMessage();
        this.valid = bordereau.isValid();

    }

    public BordereauViewData(BordereauWithoutFile bordereau) {

        this.id = bordereau.getId();
        this.status = bordereau.getStatus();
        this.fileName = bordereau.getFileName();
        this.createdDate = dateFormat.format(bordereau.getCreatedDate());
        this.createdBy = bordereau.getCreatedBy().getDisplayName();
        this.description = bordereau.getDescription();
        this.totalClaims = bordereau.getTotalClaims();
        this.fileSize = bordereau.getFileSize();
        this.processed = bordereau.isProcessed();
        this.message = bordereau.getMessage();
        this.valid = bordereau.isValid();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalClaims() {
        return totalClaims;
    }

    public void setTotalClaims(Integer totalClaims) {
        this.totalClaims = totalClaims;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
