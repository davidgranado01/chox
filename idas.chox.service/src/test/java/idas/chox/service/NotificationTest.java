package idas.chox.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.data.notifications.NotificationType;
import idas.chox.service.workflow.activities.EcdUpdate;
import idas.chox.test.BaseTest;

public class NotificationTest extends BaseTest {

    private List<ClaimResult> claimResults;
    private static final String path = "andy.20090825.1test.xml";
    
    @Before
    public void setUpClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(true);
        claimResults = loadBordereauResult(path);
    }

    @After
    public void tearDownClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(false);
    }
    
    @Test
    @Transactional
    public void testECDUpdatedNotification() throws Exception {
        
        Claim claim = claimResults.get(0).getClaim();
        claim.setClaimNumber("0001");
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        
        claimService.save(claim);

        EcdUpdate activity = (EcdUpdate) activityFactory.getActivity("ecdUpdate");
        activity.setReasonOfDelayId(1);
        activity.setEcdDate(new Date());
        activity.setSupportingNote("No delays, first ECD provided by the garage or if no ECD provided date repairs completed.");
        activity.setUpdateInsurer(true);
        activity.setSequence(1);
        activity.process(claim);

        
        Assert.assertEquals(notificationService.getNotifications(claim.getId()).size(), 1); 
        Assert.assertTrue(notificationService.getNotifications(claim.getId()).get(0).getMessage().equals("ECD Update"));
    }
    
    @Test
    @Transactional
    public void testAnomalousECDNotification() throws Exception {

        Claim claim = claimResults.get(0).getClaim();
        claim.setClaimNumber("0001");
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        
        EcdUpdate activity = (EcdUpdate) activityFactory.getActivity("ecdUpdate");
        activity.setReasonOfDelayId(1);
        activity.setEcdDate(new Date());
        activity.setSupportingNote("No delays, first ECD provided by the garage or if no ECD provided date repairs completed.");
        activity.setUpdateInsurer(true);
        activity.setSequence(1);
        activity.process(claim);
        
        notificationService.checkForAnomalies(claim, NotificationType.EcdAnomalousNotification.getType());
        
        Assert.assertEquals(notificationService.getNotifications(claim.getId()).size(), 1); 
        Assert.assertTrue(notificationService.getNotifications(claim.getId()).get(0).getMessage().equals("ECD Update"));
    }

/*
 *   Hire UPdate anomaly now generated in Action class only.....
 *      Probably better, in the long run, to move hire monitoring updates to an activity - TODO
    @Test
    @Transactional
    public void testHireMonitorigDetailNotification() throws Exception {

        Claim claim = claimResults.get(0).getClaim();
        claim.setClaimNumber("0001");
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setClaim(claim);
        hmd.setUpdateInsurer(true);        
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hmd.setIsTotalLostCheck(false);
        
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        claim.setHireMonitoringDetail(hmd);
        claimService.save(claim);
        
        hmd.setUpdateInsurer(true);        
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hmd.setIsTotalLostCheck(true);
        
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        claim.setHireMonitoringDetail(hmd);
        claimService.save(claim);
        
        Assert.assertEquals(notificationService.getNotifications(claim.getId()).size(), 1); 
        Assert.assertTrue(notificationService.getNotifications(claim.getId()).get(0).getMessage().equals("Hire Update"));
    }
*/
    
    @Test
    @Transactional
    public void testAnomalousMonitorigDetailNotification() throws Exception {

        Claim c = claimResults.get(0).getClaim();
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setClaim(c);
        hmd.setUpdateInsurer(true);        
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hmd.setIsTotalLostCheck(false);
        
        Customer cus = customerService.getCustomer(999);
        
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        c.setHireMonitoringDetail(hmd);
        c.setCustomer(cus);
        claimService.save(c);
        
        hmd.setUpdateInsurer(true);        
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hmd.setIsTotalLostCheck(true);
        
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        notificationService.checkForAnomalies(c, NotificationType.TotalLossAnomalousNotification.getType());
        
        Assert.assertEquals(notificationService.getNotifications(c.getId()).size(), 1); 
        Assert.assertTrue(notificationService.getNotifications(c.getId()).get(0).getMessage().equals("The CHO has indicated the claim is now a Total Loss"));
    }
    
    
    @Test
    @Transactional
    public void testDetectSundayAnomalous() throws Exception {

        Date date = DateHelper.getLocalDateFormat().parse("27/01/2013");
        
        Claim c = claimResults.get(0).getClaim();
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setClaim(c);
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hmd.setRepairBookInDate(date);
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        
        claimService.checkRepairBookedInDateAnomaly(c);
        notificationService.checkForAnomalies(c, NotificationType.RepairBookedInDateAnomalousNotification.getType());
        
        Assert.assertEquals(notificationService.getNotifications(c.getId()).size(), 1); 
        Assert.assertTrue(notificationService.getNotifications(c.getId()).get(0).getMessage().equals("Repair booked in on Sunday and the CHO's Customer's vehicle was driveable."));
        
    }
    
    @Test
    @Transactional
    public void testDetectWeekdayAnomalous() throws Exception {

        Date date = DateHelper.getLocalDateFormat().parse("24/01/2013");
        
        Claim c = claimResults.get(0).getClaim();
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setClaim(c);
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hmd.setRepairBookInDate(date);
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        claimService.checkRepairBookedInDateAnomaly(c);
        notificationService.checkForAnomalies(c, NotificationType.RepairBookedInDateAnomalousNotification.getType());
        
        Assert.assertEquals(notificationService.getNotifications(c.getId()).size(), 0); 
        
    }
    
    @Test
    @Transactional
    public void testDetectSaturdayAnomalous() throws Exception {

        Date date = DateHelper.getLocalDateFormat().parse("26/01/2013");
        
        Claim c = claimResults.get(0).getClaim();
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setClaim(c);
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        hmd.setRepairBookInDate(date);
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        claimService.checkRepairBookedInDateAnomaly(c);
        notificationService.checkForAnomalies(c, NotificationType.RepairBookedInDateAnomalousNotification.getType());
        
        Assert.assertEquals(notificationService.getNotifications(c.getId()).size(), 1); 
        Assert.assertTrue(notificationService.getNotifications(c.getId()).get(0).getMessage().equals("Repair booked in on Saturday and the CHO's Customer's vehicle was driveable."));
        
    }
    
    @Test
    @Transactional
    public void testDetectFridayAnomalous() throws Exception {

        Date date = DateHelper.getLocalDateFormat().parse("25/01/2013");
        
        Claim c = claimResults.get(0).getClaim();
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        
        HireMonitoringDetail hmd = new HireMonitoringDetail();
        hmd.setClaim(c);
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        
        hmd.setCreatedDate(Calendar.getInstance().getTime());
        
        hmd.setRepairBookInDate(date);
        
        hireMonitoringDetailService.saveHireMonitoringDetail(hmd);
        c.setHireMonitoringDetail(hmd);
        claimService.save(c);
        claimService.checkRepairBookedInDateAnomaly(c);
        notificationService.checkForAnomalies(c, NotificationType.RepairBookedInDateAnomalousNotification.getType());
        
        Assert.assertEquals(notificationService.getNotifications(c.getId()).size(), 1); 
        Assert.assertTrue(notificationService.getNotifications(c.getId()).get(0).getMessage().equals("Repair booked in on Friday and the CHO's Customer's vehicle was driveable."));
        
    }
    
    

    private List<ClaimResult> loadBordereauResult(String path) throws Exception {
        File file = new ClassPathResource(path).getFile();
        int totalProcessed = 0;
        List<String> choReferences = new ArrayList<String>();
        Document document = DocumentHelper.getDocumentFromFile(file);
        List<ClaimResult> claimResults = this.uploadClaimXMLService.formClaimResults(document);
        for (ClaimResult claimResult : claimResults) {
            if (this.uploadClaimXMLService.doProcessBordereauResult(claimResult, choReferences)) {
                totalProcessed++;
            }
        }
        return claimResults;
    }
}
