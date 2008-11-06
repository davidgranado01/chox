package chox.util;

import chox.data.Claim;
import chox.data.ClaimHandlingRate;
import chox.data.InsurerCountry;
import chox.data.Invoice;
import chox.data.Rental;
import chox.data.RentalPaymentRequest;
import chox.data.Supplier;
import com.filesystemsoftware.utils.DBConnectionWrapper;
import java.sql.SQLException;


public class PaymentRequestGenerator {

    public static RentalPaymentRequest createClaimHandlingInvoice(DBConnectionWrapper connection,Rental rental) throws SQLException
    {
        Supplier supplier=Supplier.instantiate(connection, rental.getSupplierID());
        Claim claim=Claim.getForRental(connection, rental);
        InsurerCountry insurerCountry=InsurerCountry.instantiate(connection, claim.getTpInsurerCountryID());
        ClaimHandlingRate rate=ClaimHandlingRate.getRate(connection, insurerCountry, supplier);
        
        if(rate==null)
            return null;
        
        RentalPaymentRequest pr=new RentalPaymentRequest();
        pr.setSupplierID(supplier.getID());
        pr.setInsurerCountryID(insurerCountry.getID());
        pr.setInvoiceType("claim handling");
        pr.setNetAmount(rate.getNetAmount());
        pr.setVatAmount(rate.getVatAmount());
        pr.setGrossAmount(rate.getGrossAmount());
        pr.setRentalID(rental.getID());
        pr.save(connection);
        
        return pr;
    }
    
    public static RentalPaymentRequest createRentalInvoice(DBConnectionWrapper connection,Rental rental) throws SQLException
    {
        Supplier supplier=Supplier.instantiate(connection, rental.getSupplierID());
        Claim claim=Claim.getForRental(connection, rental);
        InsurerCountry insurerCountry=InsurerCountry.instantiate(connection, claim.getTpInsurerCountryID());
        Invoice invoice=Invoice.getForRental(connection, rental);
        
        if(invoice==null)
            return null;
        
        RentalPaymentRequest pr=new RentalPaymentRequest();
        pr.setSupplierID(supplier.getID());
        pr.setInsurerCountryID(insurerCountry.getID());
        pr.setInvoiceType("rental");
        pr.setNetAmount(invoice.getNet());
        pr.setVatAmount(invoice.getVAT());
        pr.setGrossAmount(invoice.getGross());
        pr.setRentalID(rental.getID());
        pr.save(connection);
        
        return pr;
    }
    
    
}
