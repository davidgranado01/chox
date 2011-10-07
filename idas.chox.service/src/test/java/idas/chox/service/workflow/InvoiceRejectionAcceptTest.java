package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Invoice;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.InvoiceRejectionAccept;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-Workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class InvoiceRejectionAcceptTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    ClaimService claimService;
    @Autowired
    InsurerService insurerService;


    @Test(expected = InvalidClaimStatusException.class)
    public void testInvoiceRejectionAcceptWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        Activity activity = activityFactory.getActivity("acceptRejectedInvoice");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testInvoiceRejectionAccept() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer); 
        claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
        Invoice invoice = new Invoice();
        invoice.setDateInvoiced(new Date());
        invoice.setHireNet(BigDecimal.ONE);
        invoice.setHireVat(BigDecimal.ZERO);
        invoice.setHireGross(BigDecimal.ZERO);
        invoice.setRepairNet(BigDecimal.ONE);
        invoice.setRepairVat(BigDecimal.ZERO);
        invoice.setRepairGross(BigDecimal.ZERO);
        invoice.setEngineerFeeNet(BigDecimal.ONE);
        invoice.setEngineerFeeVat(BigDecimal.ZERO);
        invoice.setEngineerFeeGross(BigDecimal.ZERO);
        invoice.setStorageRecoveryNet(BigDecimal.ZERO);
        invoice.setStorageRecoveryVat(BigDecimal.ZERO);
        invoice.setStorageRecoveryGross(BigDecimal.ZERO);
        invoice.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        invoice.setDeductionForClaimsHandlingFee(BigDecimal.ZERO);
        invoice.setTotalNet(BigDecimal.ONE);
        invoice.setTotalVat(BigDecimal.ZERO);
        invoice.setTotalGross(BigDecimal.ZERO);
        invoice.setDiscount(BigDecimal.ZERO);
        invoice.setTotalToPay(BigDecimal.TEN);
        invoice.setFullTotalToPay(BigDecimal.TEN);
        invoice.setOriginalFullTotalToPay(BigDecimal.TEN);
        invoice.setOriginalTotalToPay(BigDecimal.TEN);
        invoice.setInsurerDiscount(BigDecimal.ZERO);
        claim.setInvoice(invoice);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        InvoiceRejectionAccept activity = (InvoiceRejectionAccept) activityFactory.getActivity("acceptRejectedInvoice");

        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_REJECTED_ACCEPTED, claim.getStatus());
    }
}
