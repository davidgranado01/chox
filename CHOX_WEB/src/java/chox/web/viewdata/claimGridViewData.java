package chox.web.viewdata;

import chox.Util.AccessibilityHelper;
import chox.model.Chorganisation;
import chox.model.Claim;
import chox.model.Customer;
import chox.model.Insurer;
import chox.model.Invoice;
import chox.model.ThirdParty;
import chox.model.WebUser;
import chox.model.Workgroup;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class claimGridViewData {

    private String supplierReference;
    private int id;
    private String claimNumber;
    private String invoiceAmount;
    private String vehicleRegistration;
    private String createdDate;
    private String lastModifiedDate;
    private String status;
    private String workgroup;
    private String reviewDate;
    private String cho;
    private String insurer;
    private String createdBy;
    private String policyNumber;
    private boolean isOwnershipEditable;
    private boolean isWorkgroupEditable;
    private String ownerName;

    public claimGridViewData(Claim claim, WebUser user) {
              
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Format dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        NumberFormat currentcyFormat = DecimalFormat.getCurrencyInstance(Locale.UK);

        Customer customer = claim.getCustomer();
        Chorganisation c = claim.getChorganisation();
        Insurer i = claim.getInsurer();
        Workgroup wg = claim.getWorkgroup();
        Invoice ivc = claim.getInvoice();
        ThirdParty thirdParty = claim.getThirdParty();
        
        this.id = claim.getId();
        this.supplierReference = claim.getChoReference();
        this.invoiceAmount = ivc == null ? "" : currentcyFormat.format(ivc.getTotalToPay());
        this.vehicleRegistration = customer == null ? "" : thirdParty.getVehicleRegistration();
        this.workgroup = wg == null ? "" : wg.getName();
        this.claimNumber = claim.getClaimNumber();
        this.createdDate = dateFormat.format(claim.getCreatedDate());
        this.lastModifiedDate = dateTimeFormat.format(claim.getLastModifiedDate());
        this.status = claim.getStatus();
        this.cho = c == null ? "" : c.getName();
        this.insurer = i == null ? "" : i.getName();
        this.policyNumber = claim.getThirdParty().getPolicyNumber();

        if(claim.getHireMonitoringDetail()!=null){
            if(claim.getHireMonitoringDetail().getNextReviewDate()!=null){
                this.reviewDate = dateFormat.format(claim.getHireMonitoringDetail().getNextReviewDate());
            }
        }
        
        /*
        String orgName = "";
        WebUser createdByUser = claim.getCreatedBy();
        
        if (createdByUser != null) {
            
            Chorganisation createdByCho = createdByUser.getChorganisation();
            Insurer createdByIns = createdByUser.getInsurer();

            if (createdByIns != null) {
                orgName = String.format("(%1$s)", createdByIns.getName());
            } else if (createdByIns != null) {
                orgName = String.format("(%1$s)", createdByCho.getName());
            }
            
            this.createdBy = String.format("%1$s %2$s %3$s", createdByUser.getFirstName(), createdByUser.getLastName(), orgName);
        }
        */
        
        this.isWorkgroupEditable = AccessibilityHelper.getIsClaimWorkgroupEditable(claim, user);
        this.isOwnershipEditable = AccessibilityHelper.getIsClaimOwnershipEditable(claim, user);

        if(claim.getClaimOwner()!=null){
            this.ownerName = claim.getClaimOwner().getDisplayName();
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

    public String getVehicleRegistration() {
        return vehicleRegistration;
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

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

