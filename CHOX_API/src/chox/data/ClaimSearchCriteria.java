/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.data;

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
    private int lineOfBusinessId;    
    private boolean isAnomalies;    
    private boolean ispenaltyChargeApplied;

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

    public int getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(int lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
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
}
