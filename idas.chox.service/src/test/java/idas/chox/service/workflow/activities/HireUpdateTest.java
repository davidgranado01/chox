package idas.chox.service.workflow.activities;


import java.util.Date;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import idas.chox.core.util.DateHelper;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class HireUpdateTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        WebUser webUser = userService.getWebUser(2);
        fakeSecurityInfoProvider.setCurrentUser(webUser);
        fakeSecurityInfoProvider.setIsINS(false);
    }


    @Test(expected = AccessDeniedException.class)
    public void testHireUpdateWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        Activity activity = activityFactory.getActivity("hireUpdate");
        activity.process(claim);
    }


    @Test(expected = Exception.class)
    public void testHireUpdateWithNoHireStartDate() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        
        HireUpdate activity = (HireUpdate) activityFactory.getActivity("hireUpdate");
        activity.process(claim);
    }


    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testHireUpdateWithUpdateInsurer() throws Exception {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        int noNotificationsOriginal = notificationService.getNotifications(claim.getId()).size();
        HireUpdate activity = (HireUpdate) activityFactory.getActivity("hireUpdate");
        Date today = new Date();
        activity.setHireStartDate(today);
        activity.setUpdateInsurer(true);
        activity.process(claim);

        // Check date has been set
        Assert.assertEquals(today.toString(), claim.getVehicleHire().getHireStart().toString());

        // Check insurer has been updated
        int noNotifications = notificationService.getNotifications(claim.getId()).size();
        
        Assert.assertEquals(noNotifications, noNotificationsOriginal+1);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testHireUpdateWithNoUpdateInsurer() throws Exception {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        int noNotificationsOriginal = notificationService.getNotifications(claim.getId()).size();
        
        HireUpdate activity = (HireUpdate) activityFactory.getActivity("hireUpdate");
        Date today = new Date();
        activity.setHireStartDate(today);
        activity.setUpdateInsurer(false);
        activity.process(claim);
        
        // Check date has been set
        Assert.assertEquals(today.toString(), claim.getVehicleHire().getHireStart().toString());
        
        // Check insurer has not been updated
        int noNotifications = notificationService.getNotifications(claim.getId()).size();
        
        Assert.assertEquals(noNotifications, noNotificationsOriginal);
        
    }
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testHireUpdateWithTime() throws Exception {

        Claim claim = claimService.getClaim(999);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        
        HireUpdate activity = (HireUpdate) activityFactory.getActivity("hireUpdate");
        Date today = new Date();
        activity.setHireStartDate(today);
        activity.setHireStartTime("12:00");
        activity.process(claim);

        // Check date has been set to merged datetime
        Date mergedDated = DateHelper.mergeTimeToDate(today, DateHelper.getTimeFormat().parse("12:00"));
        
        Assert.assertEquals(mergedDated.toString(), claim.getVehicleHire().getHireStart().toString());
        
    }
    
   
}
