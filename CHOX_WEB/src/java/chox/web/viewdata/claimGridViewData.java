/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.viewdata;

import chox.model.Chorganisation;
import chox.model.Claim;
import chox.model.Insurer;
import chox.model.VehicleHire;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Emmanuel
 */
public class claimGridViewData {
    private String supplierReference;
    private int id;
    private BigDecimal invoiceAmount;
    private String vehicleRegistration;
    private Date created;
    private String status;
    private String lineOfBusiness;
    private String cho;
    private String insurer;
    
    public claimGridViewData(Claim claim)
    {        
        VehicleHire v = claim.getVehicleHire();
        Chorganisation c = claim.getChorganisation();
        Insurer i = claim.getInsurer();
        
        this.id = claim.getId();
        this.supplierReference = claim.getChoReference();
        this.invoiceAmount = BigDecimal.ZERO; //TODO : assign  invoice amount 
        
        this.vehicleRegistration = v == null ? "" : v.getVehicleRegistration();
        this.lineOfBusiness = "";//TODO : assign lineOfBusiness
        this.created = claim.getCreatedDate();
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

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public Date getCreated() {
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

