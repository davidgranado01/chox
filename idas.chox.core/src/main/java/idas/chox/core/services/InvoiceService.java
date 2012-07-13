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
    
    public BigDecimal calculateHirePenaltyCharge(Invoice inv, String hirePercentage, Date hireStart);
    
    public BigDecimal calculateHirePenaltyCharge(Claim claim);
    
    public BigDecimal calculateRepairPenaltyCharge(Invoice inv, String repairPercentage);
    
    public BigDecimal calculateRepairPenaltyCharge(Claim claim);
    
    public String calculatedRepairPenaltyPercentage(Invoice inv);
    
    public String calculatedHirePenaltyPercentage(Invoice inv, Date hireStart);
        
    public void applyInsurerDiscounts(Claim claim, WebUser user, boolean canAddComment);
    
    public void addInsurerDiscountComment(Claim claim, BigDecimal insurerDiscountAmount, BigDecimal insurerDiscountPercentage, String insurerDiscountType, WebUser user);
    
}
