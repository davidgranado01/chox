/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.data;

import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Emmanuel
 */

public class ClaimSearchCriteria implements Serializable {

    private String supplierReference;
    private int supplierId;
    private String claimNumber;
    private String status;
    private int insurerId;
    private String vrn;
    private String invoiceNumber;
    private Date claimUploadDateFrom;
    private Date claimUploadDateTo;
    private Date invoiceUploadDateFrom;
    private Date invoiceUploadDateTo;
    private Date hireDateFrom;
    private Date hireDateTo;
    private Date lastModifiedDateFrom;
    private Date lastModifiedDateTo;    
    private int workgroupId;
    private boolean isAnomalies;
    private boolean ispenaltyChargeApplied;
    private int start;
    private int limit;
    private String sort;
    private String dir;
    private Date reviewRequiredDateFrom;
    private Date reviewRequiredDateTo;
    
    private int claimOwnerId;

    private boolean isWorkgroupCheck;
    private boolean isOwnerShipCheck;

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getReviewRequiredDateFrom() {
        return reviewRequiredDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setReviewRequiredDateFrom(Date reviewRequiredDateFrom) {
        this.reviewRequiredDateFrom = reviewRequiredDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getReviewRequiredDateTo() {
        return reviewRequiredDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setReviewRequiredDateTo(Date reviewRequiredDateTo) {
        this.reviewRequiredDateTo = reviewRequiredDateTo;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int gesupplierId) {
        this.supplierId = gesupplierId;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getVrn() {
        return vrn;
    }

    public void setVrn(String vrn) {
        this.vrn = vrn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getClaimUploadDateFrom() {
        return claimUploadDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setClaimUploadDateFrom(Date claimUploadDateFrom) {
        this.claimUploadDateFrom = claimUploadDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getClaimUploadDateTo() {
        return claimUploadDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setClaimUploadDateTo(Date claimUploadDateTo) {
        this.claimUploadDateTo = claimUploadDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getInvoiceUploadDateFrom() {
        return invoiceUploadDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setInvoiceUploadDateFrom(Date invoiceUploadDateFrom) {
        this.invoiceUploadDateFrom = invoiceUploadDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getInvoiceUploadDateTo() {
        return invoiceUploadDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setInvoiceUploadDateTo(Date invoiceUploadDateTo) {
        this.invoiceUploadDateTo = invoiceUploadDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getHireDateFrom() {
        return hireDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setHireDateFrom(Date hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getHireDateTo() {
        return hireDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setHireDateTo(Date hireDateTo) {
        this.hireDateTo = hireDateTo;
    }
    
    /*
    public int getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(int lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
    }
    */
    
    public boolean getIsAnomalies() {
        return isAnomalies;
    }

    public void setIsAnomalies(boolean isAnomalies) {
        this.isAnomalies = isAnomalies;
    }

    public boolean getIspenaltyChargeApplied() {
        return ispenaltyChargeApplied;
    }

    public void setIspenaltyChargeApplied(boolean ispenaltyChargeApplied) {
        this.ispenaltyChargeApplied = ispenaltyChargeApplied;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getLastModifiedDateFrom() {
        return lastModifiedDateFrom;
    }
    
    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setLastModifiedDateFrom(Date lastModifiedDateFrom) {
        this.lastModifiedDateFrom = lastModifiedDateFrom;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public Date getLastModifiedDateTo() {
        return lastModifiedDateTo;
    }

    @TypeConversion(converter = "chox.web.data.DateConverter")
    public void setLastModifiedDateTo(Date lastModifiedDateTo) {
        this.lastModifiedDateTo = lastModifiedDateTo;
    }

    public boolean getIsOwnerShipCheck() {
        return isOwnerShipCheck;
    }

    public void setIsOwnerShipCheck(boolean isOwnerShipCheck) {
        this.isOwnerShipCheck = isOwnerShipCheck;
    }

    public boolean getIsWorkgroupCheck() {
        return isWorkgroupCheck;
    }

    public void setIsWorkgroupCheck(boolean isWorkgroupCheck) {
        this.isWorkgroupCheck = isWorkgroupCheck;
    }

    public int getClaimOwnerId() {
        return claimOwnerId;
    }

    public void setClaimOwnerId(int claimOwnerId) {
        this.claimOwnerId = claimOwnerId;
    }
    
}
