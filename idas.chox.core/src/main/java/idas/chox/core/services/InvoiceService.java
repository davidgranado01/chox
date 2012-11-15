package idas.chox.core.services;


import idas.chox.core.model.Claim;
import idas.chox.core.model.Invoice;
import idas.chox.core.model.InvoiceOriginal;
import idas.chox.core.model.WebUser;
import idas.chox.core.xmlValidation.ClaimResult;
import java.math.BigDecimal;
import java.util.Date;

public interface InvoiceService {

    public void saveInvoiceForXMLUploader(final ClaimResult claimResult);

    public Invoice getInvoice(int invoiceId);

    public void saveInvoice(Invoice invoice);
    
    public InvoiceOriginal saveOriginalInvoice(Invoice invoice);
    
    public void deleteOriginalInvoice(Invoice invoice);

    int getNoOfRejectedInvoices(Integer reasonOfRejectionId);
    
    public int calculatePenaltyAlertQty(Invoice inv);
    
    public boolean updateAutomaticPenaltyCharge(Claim claim);
    
    public boolean setPenaltyStartToDateInvoiced(String choReference);
    
    public void updatePenaltyStartDate(Claim claim, Date autoPenaltyStart);
        
    public void applyInsurerDiscounts(Claim claim, WebUser user, boolean canAddComment);
    
    public void addInsurerDiscountComment(Claim claim, BigDecimal insurerDiscountAmount, BigDecimal insurerDiscountPercentage, String insurerDiscountType, WebUser user);
    
}
