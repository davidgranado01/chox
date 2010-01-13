package idas.chox.service;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.Notification;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.notifications.ClaimAnomalousChecker;
import idas.chox.service.notifications.EcdAnomalousNotification;
import idas.chox.service.notifications.EcdUpdatedNotification;
import idas.chox.service.notifications.HireUpdatedNotification;
import idas.chox.service.notifications.RepairBookedInOnFridayNotification;
import idas.chox.service.xml.readers.BordereauReader;
import java.io.File;
import java.util.Date;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-Notification-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-XMLReader-test.xml", "classpath:applicationContext-Workflow-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class NotificationTest {

    @Autowired
    @Qualifier("newECDAddedChecker")
    ClaimAnomalousChecker newECDAddedChecker;
    @Autowired
    @Qualifier("hireMonitoringDetailUpdatedChecker")
    ClaimAnomalousChecker hireMonitoringDetailUpdatedChecker;
    @Autowired
    UploadClaimXMLService uploadClaimXMLService;
    @Autowired
    ClaimService claimService;
    @Autowired
    BordereauReader bordereauReader;

    @Test
    public void canClaimAnomalousCheckerGetInjected() {
        Assert.assertNotNull(newECDAddedChecker);
        Assert.assertNotNull(hireMonitoringDetailUpdatedChecker);
    }

    @Test
    @Transactional
    public void testCanTriggerHireUpdatedNotification() throws Exception {

        String path = "andy.20090825.1test.xml";

        BordereauResult bordereauResult = loadBordereauResult(path);
        List<ClaimResult> claimResults = bordereauResult.getClaimResult();
        Claim c = claimResults.get(0).getClaim();
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        Assert.assertTrue(bordereauResult.isValid());
        c.AddNotification(new HireUpdatedNotification());
        Assert.assertTrue(c.getIsIsAnomalies());
    }

    @Test
    @Transactional
    public void testCanTriggerEcdUpdatedNotification() throws Exception {

        String path = "andy.20090825.1test.xml";
        BordereauResult bordereauResult = loadBordereauResult(path);

        List<ClaimResult> claimResults = bordereauResult.getClaimResult();
        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Assert.assertTrue(bordereauResult.isValid());
        Claim c = claimResults.get(0).getClaim();
        c.AddNotification(new EcdUpdatedNotification());     
        Assert.assertTrue(c.getIsIsAnomalies());

    }

    @Test
    @Transactional
    public void testCanDetectAnomalous() throws Exception {

        String path = "andy.20090825.1test.xml";
        BordereauResult bordereauResult = loadBordereauResult(path);
        List<ClaimResult> claimResults = bordereauResult.getClaimResult();
        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Assert.assertTrue(bordereauResult.isValid());

        Claim c = claimResults.get(0).getClaim();

        //make sure the testing claim have correct policy contact date
        Assert.assertEquals(2009, DateHelper.getYear(c.getPolicyHolderContactDate()));
        Assert.assertEquals(9, DateHelper.getMonth(c.getPolicyHolderContactDate()));
        Assert.assertEquals(15, DateHelper.getDate(c.getPolicyHolderContactDate()));

        //policy contect date = 15-09-2009
        //initial ECD = 30-09-2009
        //initial repair duration is 15 days
        //system allow max delayed duration = 50% of initial duration = 7.5 days

        //if the new ECD is 01-10-2009
        //delayed duration = 10 days (Compare to first ecd)
        //so this is a caim with anomalous ECD
        HireMonitoringEcd newEcd = new HireMonitoringEcd();
        newEcd.setEcdDate(DateHelper.Parse("10/10/2009"));
        c.addHireMonitoringEcd(newEcd);
        // TODO: CHECK REQUIRED
        for (Notification notification : newECDAddedChecker.getAnomalousNotifications(c)) {
            c.AddNotification(notification);
        }
        Assert.assertTrue(c.getIsIsAnomalies());
        Assert.assertEquals(1, c.getNotifications().size());

        //if the new ECD is 15-10-2009
        //delayed duration = 15 days (Compare to first ecd)
        //so this is a caim with anomalous ECD, if tha claim already conatains a notification with same type
        //if shouldn't add a new notification again
        HireMonitoringEcd newEcd2 = new HireMonitoringEcd();
        newEcd2.setEcdDate(DateHelper.Parse("12/10/2009"));
        c.addHireMonitoringEcd(newEcd2);
        // TODO: CHECK REQUIRED
        // c.AddNotifications(newECDAddedChecker.getAnomalousNotifications(c));
        Assert.assertTrue(c.getIsIsAnomalies());
        Assert.assertEquals(1, c.getNotifications().size());

        //if repair booked in date is friday
        Date friday = DateHelper.Parse("18/09/2009");
        c.getHireMonitoringDetail().setRepairBookInDate(friday);
        // TODO: CHECK REQUIRED
        // c.AddNotifications(hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(c));
        for (Notification notification : hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(c)) {
            c.AddNotification(notification);
        }
        Assert.assertTrue(c.getIsIsAnomalies());
        Assert.assertEquals(2, c.getNotifications().size());


        //Test make sure the notification saved correctly
        claimService.updateClaim(c);

        Claim savedClaim = claimService.getClaim(c.getId());
        Assert.assertTrue(savedClaim.getIsIsAnomalies());
        Assert.assertEquals(2, savedClaim.getNotifications().size());

        savedClaim.getNotifications().get(0).getType().equalsIgnoreCase(EcdAnomalousNotification.class.getSimpleName());
        savedClaim.getNotifications().get(1).getType().equalsIgnoreCase(RepairBookedInOnFridayNotification.class.getSimpleName());

    }

    private BordereauResult loadBordereauResult(String path) throws Exception {
        File file = new ClassPathResource(path).getFile();
        BordereauResult bordereauResult = bordereauReader.execute(file);
        return bordereauResult;
    }
}

