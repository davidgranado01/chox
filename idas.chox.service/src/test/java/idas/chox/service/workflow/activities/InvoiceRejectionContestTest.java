package idas.chox.service.workflow.activities;


import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.test.BaseTest;

public class InvoiceRejectionContestTest extends BaseTest {
    
    @Test(expected = AccessDeniedException.class)
    public void testInvoiceRejectionContestWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("contestRejectedInvoice");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testInvoiceRejectionContest() throws Throwable {

        Invoice invoice = invoiceService.getInvoice(999);
        
        invoice.setHireRateChargedPerDay(new BigDecimal(39.26));
        invoice.setHireNet(new BigDecimal(1719.05));
        invoice.setHireVat(new BigDecimal(343.81));
        invoice.setHireGross(new BigDecimal(2062.86));
        invoice.setTotalNet(new BigDecimal(1719.05));
        invoice.setTotalVat(new BigDecimal(343.81));
        invoice.setTotalGross(new BigDecimal(2062.86));
        invoice.setFullTotalToPay(new BigDecimal(2062.86));
        invoice.setTotalToPay(new BigDecimal(2062.86));
        
        invoice.setRepairNet(BigDecimal.ZERO);
        invoice.setRepairVat(BigDecimal.ZERO);
        invoice.setRepairGross(BigDecimal.ZERO);
        invoice.setEngineerFeeNet(BigDecimal.ZERO);
        invoice.setEngineerFeeVat(BigDecimal.ZERO);
        invoice.setEngineerFeeGross(BigDecimal.ZERO);
        invoice.setTotalLossFeeNet(BigDecimal.ZERO);
        invoice.setTotalLossFeeVat(BigDecimal.ZERO);
        invoice.setTotalLossFeeGross(BigDecimal.ZERO);
        invoice.setStorageRecoveryNet(BigDecimal.ZERO);
        invoice.setStorageRecoveryVat(BigDecimal.ZERO);
        invoice.setStorageRecoveryGross(BigDecimal.ZERO);
        invoice.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        invoice.setDeductionForClaimsHandlingFee(BigDecimal.ZERO);
        invoice.setChoDiscountFeePaid(BigDecimal.ZERO);
        invoice.setHirePenaltyCharge(BigDecimal.ZERO);
        invoice.setRepairPenaltyCharge(BigDecimal.ZERO);
        invoice.setTotalPenaltyCharge(BigDecimal.ZERO);
        invoice.setInsurerDiscount(BigDecimal.ZERO);
        
        
        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer); 
        
        Chorganisation chorganisation = chorganisationService.getChorganisation(1006);
        claim.setChorganisation(chorganisation);
        
        claim.setCustomer(customerService.getCustomer(999));
        claim.getCustomer().setVehicleClass(vehicleClassService.getVehicleClass(31));
        
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        claim.setInvoice(invoice);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        InvoiceRejectionContest activity = (InvoiceRejectionContest) activityFactory.getActivity("contestRejectedInvoice");

        try {
            activity.process(claim);
        } catch (Exception ex) {
            
        }
        
        Assert.assertEquals(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO, claim.getStatus());
    }
    
    private List<ClaimResult> loadBordereauResult(String fileName) throws Exception {
        File file = new ClassPathResource(fileName).getFile();
        Document document = DocumentHelper.getDocumentFromFile(file);
        List<ClaimResult> claimResults = this.uploadClaimXMLService.formClaimResults(document);
        return claimResults;
    }
}
