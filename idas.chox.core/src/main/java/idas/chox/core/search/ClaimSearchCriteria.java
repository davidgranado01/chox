/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.search;

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
    private String thirdPartyVrn;
    private String customerVrn;
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
    private boolean isOpenClaim;
    private int claimOwnerId;
    private boolean isWorkgroupCheck;
    private boolean isOwnerShipCheck;
    private boolean isSearched;

    public Date getReviewRequiredDateFrom() {
        return reviewRequiredDateFrom;
    }

    public void setReviewRequiredDateFrom(Date reviewRequiredDateFrom) {
        this.reviewRequiredDateFrom = reviewRequiredDateFrom;
    }

    public Date getReviewRequiredDateTo() {
        return reviewRequiredDateTo;
    }

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

    public String getCustomerVrn() {
        return customerVrn;
    }

    public void setCustomerVrn(String customerVrn) {
        this.customerVrn = customerVrn;
    }

    public String getThirdPartyVrn() {
        return thirdPartyVrn;
    }

    public void setThirdPartyVrn(String thirdPartyVrn) {
        this.thirdPartyVrn = thirdPartyVrn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getClaimUploadDateFrom() {
        return claimUploadDateFrom;
    }

    public void setClaimUploadDateFrom(Date claimUploadDateFrom) {
        this.claimUploadDateFrom = claimUploadDateFrom;
    }

    public Date getClaimUploadDateTo() {
        return claimUploadDateTo;
    }

    public void setClaimUploadDateTo(Date claimUploadDateTo) {
        this.claimUploadDateTo = claimUploadDateTo;
    }

    public Date getInvoiceUploadDateFrom() {
        return invoiceUploadDateFrom;
    }

    public void setInvoiceUploadDateFrom(Date invoiceUploadDateFrom) {
        this.invoiceUploadDateFrom = invoiceUploadDateFrom;
    }

    public Date getInvoiceUploadDateTo() {
        return invoiceUploadDateTo;
    }

    public void setInvoiceUploadDateTo(Date invoiceUploadDateTo) {
        this.invoiceUploadDateTo = invoiceUploadDateTo;
    }

    public Date getHireDateFrom() {
        return hireDateFrom;
    }

    public void setHireDateFrom(Date hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    public Date getHireDateTo() {
        return hireDateTo;
    }

    public void setHireDateTo(Date hireDateTo) {
        this.hireDateTo = hireDateTo;
    }

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

    public Date getLastModifiedDateFrom() {
        return lastModifiedDateFrom;
    }

    public void setLastModifiedDateFrom(Date lastModifiedDateFrom) {
        this.lastModifiedDateFrom = lastModifiedDateFrom;
    }

    public Date getLastModifiedDateTo() {
        return lastModifiedDateTo;
    }

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

    public boolean getIsOpenClaim() {
        return isOpenClaim;
    }

    public void setIsOpenClaim(boolean isOpenClaim) {
        this.isOpenClaim = isOpenClaim;
    }

    public boolean getIsSearched() {
        return isSearched;
    }

    public void setIsSearched(boolean isSearched) {
        this.isSearched = isSearched;
    }
}
