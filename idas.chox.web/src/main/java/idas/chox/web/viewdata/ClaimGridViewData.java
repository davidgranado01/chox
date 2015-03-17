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
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.util.AccessibilityHelper;

public class ClaimGridViewData {

    private final String supplierReference;
    private final int id;
    private final String claimNumber;
    private final String claimType;
    private final String invoiceAmount;
    private final String createdDate;
    private final String statusModifiedDate;
    private final String status;
    private final String workgroup;
    private final String reviewDate;
    private final String cho;
    private final String choBranding;
    private final String insurer;
    private final String createdBy;
    private final String policyNumber;
    private final String invoiceUploadDate;
    private final boolean isOwnershipEditable;
    private final boolean isWorkgroupEditable;
    private final String ownerName;
    private final String choOwnerName;
    private final int noAttachments;

    public ClaimGridViewData(Claim claim, WebUser user) {

        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Format dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        NumberFormat currencyFormat = DecimalFormat.getCurrencyInstance(Locale.UK);

        Chorganisation chorg = claim.getChorganisation();
        Insurer ins = claim.getInsurer();
        Workgroup wg = claim.getWorkgroup();
        Invoice invoice = claim.getInvoice();

        this.id = claim.getId();
        this.supplierReference = claim.getChoReference();
        this.claimType = claim.getClaimType().toString();
        this.invoiceAmount = invoice == null ? "" : currencyFormat.format(invoice.getTotalToPay());
        this.workgroup = wg == null ? "" : wg.getName();
        this.claimNumber = claim.getClaimNumber();
        this.createdDate = dateFormat.format(claim.getCreatedDate());
        this.createdBy = claim.getCreatedBy().getDisplayName();
        this.statusModifiedDate = claim.getStatusModifiedDate() == null ? null : dateTimeFormat.format(claim.getStatusModifiedDate());

        this.status = claim.getStatus();
        this.cho = chorg == null ? "" : chorg.getName();
        this.choBranding = chorg == null ? "" : chorg.getBranding().getDescription();
        this.insurer = ins == null ? "" : ins.getName();
        this.policyNumber = claim.getThirdParty().getPolicyNumber();

        this.invoiceUploadDate = invoice == null ? "" : dateTimeFormat.format(invoice.getCreatedDate());
        
        this.reviewDate = (claim.getHireMonitoringDetail() != null && claim.getHireMonitoringDetail().getNextReviewDate() != null)
                ? dateFormat.format(claim.getHireMonitoringDetail().getNextReviewDate()) : null;

        this.isWorkgroupEditable = AccessibilityHelper.getIsClaimWorkgroupEditable(claim, user);
        this.isOwnershipEditable = AccessibilityHelper.getIsClaimOwnershipEditable(claim, user);

        this.ownerName = claim.getClaimOwner() == null ? null : claim.getClaimOwner().getDisplayName();
        this.choOwnerName = claim.getSupplierClaimOwner() == null ? null : claim.getSupplierClaimOwner().getDisplayName();
        
        this.noAttachments = claim.getNoAttachments();
    }

    public boolean isIsOwnershipEditable() {
        return isOwnershipEditable;
    }

    public boolean isIsWorkgroupEditable() {
        return isWorkgroupEditable;
    }


    public String getOwnerName() {
        return ownerName;
    }

    public String getchoOwnerName() {
        return choOwnerName;
    }

    public String getPolicyNumber() {
        return policyNumber;
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


    public String getCho() {
        return cho;
    }

    public String getChoBranding() {
        return choBranding;
    }

    public String getInsurer() {
        return insurer;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getStatusModifiedDate() {
        return statusModifiedDate;
    }

    public String getInvoiceUploadDate() {
        return invoiceUploadDate;
    }

    public String getClaimType() {
        return claimType;
    }

    public int getNoAttachments() {
        return noAttachments;
    }

}
