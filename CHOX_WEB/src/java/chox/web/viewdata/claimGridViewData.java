/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.viewdata;

import chox.model.Chorganisation;
import chox.model.Claim;
import chox.model.Customer;
import chox.model.Insurer;
import chox.model.Invoice;
import chox.model.LineOfBusiness;
import chox.model.ThirdParty;
import chox.model.WebUser;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author Emmanuel
 */
public class claimGridViewData {

    private String supplierReference;
    private int id;
    private String claimNumber;
    private String invoiceAmount;
    private String vehicleRegistration;
    private String createdDate;
    private String lastModifiedDate;
    private String status;
    private String lineOfBusiness;
    private String cho;
    private String insurer;
    private String createdBy;

    public claimGridViewData(Claim claim) {       
              
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Format dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        NumberFormat currentcyFormat = DecimalFormat.getCurrencyInstance(Locale.UK);

        Customer customer = claim.getCustomer();
        Chorganisation c = claim.getChorganisation();
        Insurer i = claim.getInsurer();
        LineOfBusiness lob = claim.getLineOfBusiness();
        Invoice ivc = claim.getInvoice();
        ThirdParty thirdParty = claim.getThirdParty();
        
        this.id = claim.getId();
        this.supplierReference = claim.getChoReference();
        this.invoiceAmount = ivc == null ? "" : currentcyFormat.format(ivc.getTotalToPay());
        //this.vehicleRegistration = customer == null ? "" : customer.getVehicleRegistration();
        this.vehicleRegistration = customer == null ? "" : thirdParty.getVehicleRegistration();
        this.lineOfBusiness = lob == null ? "" : lob.getName();
        this.claimNumber = claim.getClaimNumber();
        this.createdDate = dateFormat.format(claim.getCreatedDate());
        this.lastModifiedDate = dateTimeFormat.format(claim.getLastModifiedDate());
        // this.lastModifiedDate = claim.getLastModifiedDate().toString();
        this.status = claim.getStatus();
        this.cho = c == null ? "" : c.getName();
        this.insurer = i == null ? "" : i.getName();//TODO : assign insurer
        String orgName = "";
        WebUser user = claim.getCreatedBy();
        if (user != null) {
            Chorganisation cho = user.getChorganisation();
            Insurer ins = user.getInsurer();

            if (ins != null) {
                orgName = String.format("(%1$s)", ins.getName());
            } else if (cho != null) {
                orgName = String.format("(%1$s)", cho.getName());
            }
            this.createdBy = String.format("%1$s %2$s %3$s", user.getFirstName(), user.getLastName(), orgName);
        }
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

    public String getLineOfBusiness() {
        return lineOfBusiness;
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

