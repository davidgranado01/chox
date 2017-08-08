package idas.chox.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.Notification;
import idas.chox.core.util.DateHelper;
import idas.chox.service.workflow.activities.EcdUpdate;
import idas.chox.test.BaseTest;

public class NotificationTest extends BaseTest {

    private static final String PATH = "andy.20090825.1test.xml";
    
    @Before
    public void setUpClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(true);
        loadBordereauResult(PATH);
    }

    @After
    public void tearDownClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(false);
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void testECDUpdatedNotification() throws Exception {
        
        Claim claim = claimService.getClaimByCHOReferenceNumber("CF125341");
        
        EcdUpdate activity = (EcdUpdate) activityFactory.getActivity("ecdUpdate");
        activity.setReasonOfDelayId(1);
        activity.setEcdDate(new Date());
        activity.setSupportingNote("No delays, first ECD provided by the garage or if no ECD provided date repairs completed.");
        activity.setUpdateInsurer(true);
        activity.setSequence(1);
        activity.process(claim);

        List<Notification> notifications = claim.getNotifications();
        Assert.assertEquals(1, notifications.size()); 
        Assert.assertTrue(notifications.get(0).getMessage().equals("ECD Update"));

        // Alternatively, need to flush to get notifications directly from notificationService
        this.sessionFactory.getObject().getCurrentSession().flush();
        notifications = notificationService.getNotifications(claim.getId());
        Assert.assertEquals(1, notifications.size()); 
        Assert.assertTrue(notifications.get(0).getMessage().equals("ECD Update"));
    }
    
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void testAnomalousECDNotification() throws Exception {

        Claim claim = claimService.getClaimByCHOReferenceNumber("CF125341");
        
        EcdUpdate activity = (EcdUpdate) activityFactory.getActivity("ecdUpdate");
        activity.setReasonOfDelayId(1);
        activity.setEcdDate(new Date());
        activity.setSupportingNote("No delays, first ECD provided by the garage or if no ECD provided date repairs completed.");
        activity.setUpdateInsurer(true);
        activity.setSequence(1);
        activity.process(claim);
                
        List<Notification> notifications = claim.getNotifications();
        Assert.assertEquals(1, notifications.size()); 
        Assert.assertTrue(notifications.get(0).getMessage().equals("ECD Update"));
    }

/*
 *   Hire Update anomaly now generated in Action class only.....
 *      Probably better to move hire monitoring updates to an activity - TODO
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
    @Transactional(readOnly = false)
    public void testAnomalousMonitorigDetailNotification() throws Exception {

        Claim c = claimService.getClaimByCHOReferenceNumber("CF125341");
        
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
        claimService.checkTotalLossAnomaly(c);
        
        List<Notification> notifications = c.getNotifications();
        Assert.assertEquals(1, notifications.size()); 
        Assert.assertTrue(notifications.get(0).getMessage().equals("The CHO has indicated the claim is now a Total Loss"));
    }
    
    
    @Test
    @Transactional(readOnly = false)
    public void testDetectSundayAnomalous() throws Exception {

        Date date = DateHelper.getLocalDateFormat().parse("27/01/2013");
        
        Claim c = claimService.getClaimByCHOReferenceNumber("CF125341");
        
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
        
        List<Notification> notifications = c.getNotifications();
        Assert.assertEquals(1, notifications.size()); 
        Assert.assertTrue(notifications.get(0).getMessage().equals("Repair booked in on Sunday and the CHO's Customer's vehicle was driveable."));
        
    }
    
    @Test
    @Transactional(readOnly = false)
    public void testDetectWeekdayAnomalous() throws Exception {

        Date date = DateHelper.getLocalDateFormat().parse("24/01/2013");
        
        Claim c = claimService.getClaimByCHOReferenceNumber("CF125341");
        
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
        
        List<Notification> notifications = c.getNotifications();
        Assert.assertEquals(0, notifications.size()); 
        
    }
    
    @Test
    @Transactional(readOnly = false)
    public void testDetectSaturdayAnomalous() throws Exception {

        Date date = DateHelper.getLocalDateFormat().parse("26/01/2013");
        
        Claim c = claimService.getClaimByCHOReferenceNumber("CF125341");
        
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
        
        List<Notification> notifications = c.getNotifications();
        Assert.assertEquals(1, notifications.size()); 
        Assert.assertTrue(notifications.get(0).getMessage().equals("Repair booked in on Saturday and the CHO's Customer's vehicle was driveable."));
        
    }
    
    @Test
    @Transactional(readOnly = false)
    public void testDetectFridayAnomalous() throws Exception {

        Date date = DateHelper.getLocalDateFormat().parse("25/01/2013");
        
        Claim c = claimService.getClaimByCHOReferenceNumber("CF125341");
        
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
        
        List<Notification> notifications = c.getNotifications();
        Assert.assertEquals(1, notifications.size()); 
        Assert.assertTrue(notifications.get(0).getMessage().equals("Repair booked in on Friday and the CHO's Customer's vehicle was driveable."));
        
    }
    
    

    private boolean loadBordereauResult(String path) throws Exception {
        File testFile = new ClassPathResource(path).getFile();
        boolean uploadStatus = uploadClaimXMLService.saveUploadedFile(testFile, path);
        Assert.assertTrue(uploadStatus);
        
        Integer id = (bordereauService.getBordereauByFileName(path)).getId();
        Assert.assertNotNull(id);
        
        boolean processStatus = uploadClaimXMLService.processFile(id, new HashMap());
        Assert.assertTrue(processStatus); 

        return processStatus;
    }
}
