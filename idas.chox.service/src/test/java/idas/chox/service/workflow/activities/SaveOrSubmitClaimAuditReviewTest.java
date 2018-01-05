package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimAuditReview;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class SaveOrSubmitClaimAuditReviewTest extends BaseTest {

    @Test(expected = AccessDeniedException.class)
    public void testSaveOrSubmitClaimAuditReviewWithInvalidStatus() throws Exception {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        Activity activity = activityFactory.getActivity("saveOrSubmitClaimAuditReview");
        activity.process(claim);
    }

    @Test
    public void testSaveClaimAuditFailure() throws Throwable {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);

        SaveOrSubmitClaimAuditReview activity = (SaveOrSubmitClaimAuditReview) activityFactory.getActivity("saveOrSubmitClaimAuditReview");

        activity.setNameOfActivity("saveClaimAuditReview");
        activity.setHireDuration(10);
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("Claim Audit Review Object is null. Can not update Audit Review.", ex.getMessage());
        }
    }

    @Test
    public void testSaveClaimAudit() throws Throwable {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);

        SaveOrSubmitClaimAuditReview activity = (SaveOrSubmitClaimAuditReview) activityFactory.getActivity("saveOrSubmitClaimAuditReview");
        ClaimAuditReview claimAuditReview = new ClaimAuditReview();
        claim.setClaimAuditReview(claimAuditReview);

        activity.setClaimAuditReview(claimAuditReview);
        activity.setNameOfActivity("saveClaimAuditReview");
        activity.setHireDuration(10);
        activity.process(claim);

        Assert.assertEquals(10, claim.getClaimAuditReview().getHireDuration().intValue());
    }

    @Test
    public void testSubmitClaimAuditFailure() throws Throwable {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);

        SaveOrSubmitClaimAuditReview activity = (SaveOrSubmitClaimAuditReview) activityFactory.getActivity("saveOrSubmitClaimAuditReview");
        ClaimAuditReview claimAuditReview = new ClaimAuditReview();
        claim.setClaimAuditReview(claimAuditReview);

        activity.setClaimAuditReview(claimAuditReview);
        activity.setNameOfActivity("submitClaimAuditReview");
        activity.setClaimTypeId(1);
        activity.setWhoManagedRepair("CHO");
        activity.setTotalLossId(1);
        activity.setCustomerVehicleClassId(34);
        activity.setHireVehicleClassId(34);
        activity.setHireDuration(12);
        activity.setHireDurationAcceptableId(1);
        activity.setTotalHireCost(BigDecimal.ZERO);
        activity.setHireLeakageId(2);
        activity.setTotalRepairCost(BigDecimal.ZERO);
        activity.setPenaltyChargesPaid(BigDecimal.ZERO);
        activity.setStorageClaimedId(1);
        activity.setRecoveryClaimedId(2);

        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("'If Yes, correctly so' for 'Storage Claimed' is null. Can not update Audit Review.", ex.getMessage());
        }
    }

    @Test
    public void testSubmitClaimAudit() throws Throwable {
        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_RECEIVED);

        SaveOrSubmitClaimAuditReview activity = (SaveOrSubmitClaimAuditReview) activityFactory.getActivity("saveOrSubmitClaimAuditReview");
        ClaimAuditReview claimAuditReview = new ClaimAuditReview();
        claim.setClaimAuditReview(claimAuditReview);

        activity.setClaimAuditReview(claimAuditReview);
        activity.setNameOfActivity("submitClaimAuditReview");
        activity.setClaimTypeId(1);
        activity.setWhoManagedRepair("CHO");
        activity.setTotalLossId(1);
        activity.setCustomerVehicleClassId(34);
        activity.setHireVehicleClassId(34);
        activity.setHireDuration(12);
        activity.setHireDurationAcceptableId(1);
        activity.setTotalHireCost(BigDecimal.ZERO);
        activity.setHireLeakageId(2);
        activity.setTotalRepairCost(BigDecimal.ZERO);
        activity.setPenaltyChargesPaid(BigDecimal.ZERO);
        activity.setHireLeakageCost(BigDecimal.ZERO);
        activity.setExceededRepairCost(BigDecimal.ZERO);
        activity.setNonABPGuidelineRepairLabourRate(BigDecimal.ZERO);
        activity.setStorageClaimedId(2);
        activity.setRecoveryClaimedId(2);
        activity.process(claim);

        Assert.assertEquals(12, claim.getClaimAuditReview().getHireDuration().intValue());
    }
}
