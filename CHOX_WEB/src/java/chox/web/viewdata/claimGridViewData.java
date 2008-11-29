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
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
    private String created;
    private String status;
    private String lineOfBusiness;
    private String cho;
    private String insurer;
    
    public claimGridViewData(Claim claim)
    {        
        Format dateFormat = new SimpleDateFormat("dd/MM/yyyy") ;
        NumberFormat currentcyFormat = DecimalFormat.getCurrencyInstance(Locale.UK);
               
        Customer customer = claim.getCustomer();
        Chorganisation c = claim.getChorganisation();
        Insurer i = claim.getInsurer();
        LineOfBusiness lob = claim.getLineOfBusiness();
        Invoice ivc = claim.getInvoice();
        
        this.id = claim.getId();
        this.supplierReference = claim.getChoReference();
        this.invoiceAmount = ivc == null ? "" : currentcyFormat.format(ivc.getTotalToPay());        
        this.vehicleRegistration = customer == null ? "" : customer.getVehicleRegistration();
        this.lineOfBusiness = lob == null ? "" : lob.getName();
        this.claimNumber = claim.getClaimNumber();
        this.created = dateFormat.format(claim.getCreatedDate());
        this.status = claim.getStatus();
        this.cho = c == null ? "" : c.getName();
        this.insurer = i == null ? "" : i.getName();//TODO : assign insurer
            
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

    public String getCreated() {
        return created;
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
    

}

