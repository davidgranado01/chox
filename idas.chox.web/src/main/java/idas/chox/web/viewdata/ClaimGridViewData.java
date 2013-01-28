package idas.chox.web.viewdata;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.util.AccessibilityHelper;

public class ClaimGridViewData {

    private String supplierReference;
    private int id;
    private String claimNumber;
    private String claimType;
    private String invoiceAmount;
    private String createdDate;
    private String statusModifiedDate;
    private String status;
    private String workgroup;
    private String reviewDate;
    private String cho;
    private String insurer;
    private String createdBy;
    private String policyNumber;
    private String invoiceUploadDate;
    private boolean isOwnershipEditable;
    private boolean isWorkgroupEditable;
    private String ownerName;
    private String choOwnerName;

    public ClaimGridViewData(Claim claim, WebUser user) {

        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Format dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        NumberFormat currentcyFormat = DecimalFormat.getCurrencyInstance(Locale.UK);

        Chorganisation c = claim.getChorganisation();
        Insurer i = claim.getInsurer();
        Workgroup wg = claim.getWorkgroup();
        Invoice ivc = claim.getInvoice();
        ThirdParty thirdParty = claim.getThirdParty();
        Invoice invoice = claim.getInvoice();

        this.id = claim.getId();
        this.supplierReference = claim.getChoReference();
        this.claimType = claim.getClaimType().toString();
        this.invoiceAmount = ivc == null ? "" : currentcyFormat.format(ivc.getTotalToPay());
        this.workgroup = wg == null ? "" : wg.getName();
        this.claimNumber = claim.getClaimNumber();
        this.createdDate = dateFormat.format(claim.getCreatedDate());
        if (claim.getStatusModifiedDate() != null) {
            this.statusModifiedDate = dateTimeFormat.format(claim.getStatusModifiedDate());
        }

        this.status = claim.getStatus();
        this.cho = c == null ? "" : c.getName();
        this.insurer = i == null ? "" : i.getName();
        this.policyNumber = claim.getThirdParty().getPolicyNumber();

        this.invoiceUploadDate = invoice == null ? "" : dateTimeFormat.format(invoice.getCreatedDate());
        
        if (claim.getHireMonitoringDetail() != null) {
            if (claim.getHireMonitoringDetail().getNextReviewDate() != null) {
                this.reviewDate = dateFormat.format(claim.getHireMonitoringDetail().getNextReviewDate());
            }
        }

        this.isWorkgroupEditable = AccessibilityHelper.getIsClaimWorkgroupEditable(claim, user);
        this.isOwnershipEditable = AccessibilityHelper.getIsClaimOwnershipEditable(claim, user);

        if (claim.getClaimOwner() != null) {
            this.ownerName = claim.getClaimOwner().getDisplayName();
        }
        if (claim.getSupplierClaimOwner() != null) {
            this.choOwnerName = claim.getSupplierClaimOwner().getDisplayName();
        }
    }

    public boolean isIsOwnershipEditable() {
        return isOwnershipEditable;
    }

    public void setIsOwnershipEditable(boolean isOwnershipEditable) {
        this.isOwnershipEditable = isOwnershipEditable;
    }

    public boolean isIsWorkgroupEditable() {
        return isWorkgroupEditable;
    }

    public void setIsWorkgroupEditable(boolean isWorkgroupEditable) {
        this.isWorkgroupEditable = isWorkgroupEditable;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getchoOwnerName() {
        return choOwnerName;
    }

    public void setChoOwnerName(String choOwnerName) {
        this.choOwnerName = choOwnerName;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public int getId() {
        return id;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getStatus() {
        return status;
    }

    public String getWorkgroup() {
        return workgroup;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getCho() {
        return cho;
    }

    public String getInsurer() {
        return insurer;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getStatusModifiedDate() {
        return statusModifiedDate;
    }

    public void setStatusModifiedDate(String statusModifiedDate) {
        this.statusModifiedDate = statusModifiedDate;
    }

    public String getInvoiceUploadDate() {
        return invoiceUploadDate;
    }

    public String getClaimType() {
        return claimType;
    }

    public void setClaimType(String claimType) {
        this.claimType = claimType;
    }

}

