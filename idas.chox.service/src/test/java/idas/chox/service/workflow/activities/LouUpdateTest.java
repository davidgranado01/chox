package idas.chox.service.workflow.activities;


import java.util.Date;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;
import java.math.BigDecimal;

public class LouUpdateTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        WebUser webUser = userService.getWebUser(2);
        fakeSecurityInfoProvider.setCurrentUser(webUser);
        fakeSecurityInfoProvider.setIsINS(false);
    }


    @Test(expected = AccessDeniedException.class)
    public void testLouUpdateWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        Activity activity = activityFactory.getActivity("louUpdate");
        activity.process(claim);
    }


    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testLouUpdateWithUpdateInsurer() throws Exception {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        int noNotificationsOriginal = notificationService.getNotifications(claim.getId()).size();
        LouUpdate activity = (LouUpdate) activityFactory.getActivity("louUpdate");
        Date today = new Date();
        activity.setChoManagingRepair(Boolean.TRUE);
        activity.setImeName("ImeName");
        activity.setInspectionBookedDate(today);
        activity.setInspectionDate(today);
        activity.setInsurerManagingRepair(Boolean.TRUE);
        activity.setLabourCost(BigDecimal.TEN);
        activity.setLabourHours(BigDecimal.TEN);
        activity.setLabourRate(BigDecimal.TEN);
        activity.setNonProvisionReason("no reason");
        activity.setRepairAuthorisedDate(today);
        activity.setRepairBookedInDate(today);
        activity.setRepairCommencedDate(today);
        activity.setRepairCompletionDate(today);
        activity.setRepairOnly(Boolean.TRUE);
        activity.setRepairerName("RepairerName");
        activity.setTotalLoss(Boolean.TRUE);
        activity.setTotalLossAcceptedDate(today);
        activity.setTotalLossIssuedDate(today);
        activity.setTotalLossMadeDate(today);
        activity.setTotalLossReceivedDate(today);
        activity.setVatRegistered(Boolean.TRUE);
        activity.setUpdateInsurer(true);
        activity.setWhoIsSendingPAVifTL("CHO");
        activity.setEngineersReportSentDate(today);
        activity.process(claim);

        // Check data has been updated
        Assert.assertEquals(claim.isManagingRepair(), Boolean.TRUE);
        Assert.assertEquals(claim.getHireMonitoringDetail().getNameOfIme(), "ImeName");
        Assert.assertEquals(claim.getHireMonitoringDetail().getInspectionBookedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getInspectionDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().isIsNFInsurerManagingRepair(), true);
        Assert.assertEquals(claim.getHireMonitoringDetail().getLabourCost(), BigDecimal.TEN);
        Assert.assertEquals(claim.getHireMonitoringDetail().getLabourHour(), BigDecimal.TEN);
        Assert.assertEquals(claim.getHireMonitoringDetail().getLabourRate(), BigDecimal.TEN);
        Assert.assertEquals(claim.getHireMonitoringDetail().getNonProvisionReason(), "no reason");
        Assert.assertEquals(claim.getHireMonitoringDetail().getRepairAuthorisedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getRepairBookInDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getRepairCommencedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getRepairCompletionDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().isIsRepairOnlyCheck(), true);
        Assert.assertEquals(claim.getHireMonitoringDetail().getNameOfRepairer(), "RepairerName");
        Assert.assertEquals(claim.getHireMonitoringDetail().isIsTotalLostCheck(), true);
        Assert.assertEquals(claim.getHireMonitoringDetail().getTotalLossOfferAcceptedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getTotalLossOfferCheckIssuedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getTotalLossOfferMadeDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getClientVatRegistered(), true);
        Assert.assertEquals(claim.getHireMonitoringDetail().getWhoIsSendingPav(), "CHO");
        Assert.assertEquals(claim.getHireMonitoringDetail().getEngineersReportSentDate(), today);

        // Check insurer has been updated
        int noNotifications = notificationService.getNotifications(claim.getId()).size();
        
        Assert.assertEquals(noNotifications, noNotificationsOriginal+1);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testLouUpdateWithNoUpdateInsurer() throws Exception {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        int noNotificationsOriginal = notificationService.getNotifications(claim.getId()).size();
        
        LouUpdate activity = (LouUpdate) activityFactory.getActivity("louUpdate");
        Date today = new Date();
        activity.setChoManagingRepair(Boolean.TRUE);
        activity.setImeName("ImeName");
        activity.setInspectionBookedDate(today);
        activity.setInspectionDate(today);
        activity.setInsurerManagingRepair(Boolean.TRUE);
        activity.setLabourCost(BigDecimal.TEN);
        activity.setLabourHours(BigDecimal.TEN);
        activity.setLabourRate(BigDecimal.TEN);
        activity.setNonProvisionReason("no reason");
        activity.setRepairAuthorisedDate(today);
        activity.setRepairBookedInDate(today);
        activity.setRepairCommencedDate(today);
        activity.setRepairCompletionDate(today);
        activity.setRepairOnly(Boolean.TRUE);
        activity.setRepairerName("RepairerName");
        activity.setTotalLoss(Boolean.TRUE);
        activity.setTotalLossAcceptedDate(today);
        activity.setTotalLossIssuedDate(today);
        activity.setTotalLossMadeDate(today);
        activity.setTotalLossReceivedDate(today);
        activity.setVatRegistered(Boolean.TRUE);
        activity.setUpdateInsurer(false);
        activity.setWhoIsSendingPAVifTL("CHO");
        activity.setEngineersReportSentDate(today);
        activity.process(claim);
        
        // Check date has been set
        Assert.assertEquals(claim.isManagingRepair(), Boolean.TRUE);
        Assert.assertEquals(claim.getHireMonitoringDetail().getNameOfIme(), "ImeName");
        Assert.assertEquals(claim.getHireMonitoringDetail().getInspectionBookedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getInspectionDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().isIsNFInsurerManagingRepair(), true);
        Assert.assertEquals(claim.getHireMonitoringDetail().getLabourCost(), BigDecimal.TEN);
        Assert.assertEquals(claim.getHireMonitoringDetail().getLabourHour(), BigDecimal.TEN);
        Assert.assertEquals(claim.getHireMonitoringDetail().getLabourRate(), BigDecimal.TEN);
        Assert.assertEquals(claim.getHireMonitoringDetail().getNonProvisionReason(), "no reason");
        Assert.assertEquals(claim.getHireMonitoringDetail().getRepairAuthorisedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getRepairBookInDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getRepairCommencedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getRepairCompletionDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().isIsRepairOnlyCheck(), true);
        Assert.assertEquals(claim.getHireMonitoringDetail().getNameOfRepairer(), "RepairerName");
        Assert.assertEquals(claim.getHireMonitoringDetail().isIsTotalLostCheck(), true);
        Assert.assertEquals(claim.getHireMonitoringDetail().getTotalLossOfferAcceptedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getTotalLossOfferCheckIssuedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getTotalLossOfferMadeDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getTotalLossOfferCheckReceivedDate().toString(), today.toString());
        Assert.assertEquals(claim.getHireMonitoringDetail().getClientVatRegistered(), true);
        Assert.assertEquals(claim.getHireMonitoringDetail().getWhoIsSendingPav(), "CHO");
        Assert.assertEquals(claim.getHireMonitoringDetail().getEngineersReportSentDate(), today);
        
        // Check insurer has not been updated
        int noNotifications = notificationService.getNotifications(claim.getId()).size();
        
        Assert.assertEquals(noNotifications, noNotificationsOriginal);
    }
}
