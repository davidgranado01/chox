/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.viewdata;

import chox.model.Chorganisation;
import chox.model.Claim;
import chox.model.Insurer;
import chox.model.Invoice;
import chox.model.LineOfBusiness;
import chox.model.VehicleHire;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * @author Emmanuel
 */
public class claimGridViewData {
    private String supplierReference;
    private int id;
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
        NumberFormat currentcyFormat = DecimalFormat.getCurrencyInstance();
        
        VehicleHire v = claim.getVehicleHire();
        Chorganisation c = claim.getChorganisation();
        Insurer i = claim.getInsurer();
        LineOfBusiness lob = claim.getLineOfBusiness();
        Invoice ivc = claim.getInvoice();
        
        this.id = claim.getId();
        this.supplierReference = claim.getChoReference();
        this.invoiceAmount = ivc == null ? "" : currentcyFormat.format(ivc.getTotalToPay());        
        this.vehicleRegistration = v == null ? "" : v.getVehicleRegistration();
        this.lineOfBusiness = lob == null ? "" : lob.getName();
        
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
    

}

