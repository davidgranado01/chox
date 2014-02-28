package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

/**
 *
 * @author John
 */
public class UpdateManualInvoiceAgreeQuantumTest  extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testUpdateManualInvoiceWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        activity.process(claim);
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_1() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        claim.setLiability(LiabilityStatus.LIABILITY_ACCEPTED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_2() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        claim.setLiability(LiabilityStatus.LIABILITY_ACCEPTED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_3() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
        claim.setLiability(LiabilityStatus.LIABILITY_ACCEPTED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_4() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        claim.setLiability(LiabilityStatus.LIABILITY_SPLIT);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_5() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        claim.setLiability(LiabilityStatus.LIABILITY_SPLIT);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_6() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
        claim.setLiability(LiabilityStatus.LIABILITY_SPLIT);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

@Test
    public void testUpdateManualInvoiceAgreeQuantum_7() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        claim.setLiability(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_8() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        claim.setLiability(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_9() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
        claim.setLiability(LiabilityStatus.PROCEED_WITHOUT_PREJUDICE);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_10() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        claim.setLiability(LiabilityStatus.LIABILITY_DISPUTED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_11() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        claim.setLiability(LiabilityStatus.LIABILITY_DISPUTED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_12() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
        claim.setLiability(LiabilityStatus.LIABILITY_DISPUTED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_13() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        claim.setLiability(LiabilityStatus.LIABILITY_REPUDIATED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_14() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        claim.setLiability(LiabilityStatus.LIABILITY_REPUDIATED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_15() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
        claim.setLiability(LiabilityStatus.LIABILITY_REPUDIATED);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_16() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        claim.setLiability(LiabilityStatus.LIABILITY_NULL);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_17() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        claim.setLiability(LiabilityStatus.LIABILITY_NULL);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_18() throws Throwable {

        Claim claim = new Claim();
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
        claim.setLiability(LiabilityStatus.LIABILITY_NULL);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_19() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        claim.setLiability(LiabilityStatus.LIABILITY_UNKNOWN);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_20() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_CONTESTED);
        claim.setLiability(LiabilityStatus.LIABILITY_UNKNOWN);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

    @Test
    public void testUpdateManualInvoiceAgreeQuantum_21() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setClaimType(ClaimType.INSURER_INVOICE);
        claim.setInsurer(insurer);
        claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
        claim.setLiability(LiabilityStatus.LIABILITY_UNKNOWN);
     
        Activity activity = activityFactory.getActivity("updateManualInvoiceAgreeQuantum");
        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_LIABILITY_RESOLUTION, claim.getStatus());
    }

}
