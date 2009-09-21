/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import chox.Util.DateHelper;
import chox.model.notifications.*;
import chox.services.ClaimService;
import chox.services.UploadClaimXMLService;
import chox.xmlValidation.model.BordereauResult;
import chox.services.ClaimResult;
import java.io.File;
import java.io.IOException;
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

/**
 *
 * @author Emmanuel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-Notification.xml", "classpath:applicationContext.xml","classpath:applicationContext-services.xml"})
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

     @Test
     public void canClaimAnomalousCheckerGetInjected(){
         Assert.assertNotNull(newECDAddedChecker);
         Assert.assertNotNull(hireMonitoringDetailUpdatedChecker);
     }

    @Test
    @Transactional
    public void testCanTriggerHireUpdatedNotification() throws IOException {

        String fileName = "andy.20090825.1test.xml";
        String path = "/chox/testFile/andy.20090825.1test.xml";
        File file = new ClassPathResource(path).getFile();
        Assert.assertNotNull(file);

        BordereauResult bordereauResult = uploadClaimXMLService.processBordereau(file, fileName);
        List<ClaimResult> claimResults = bordereauResult.getClaimResult();
        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Assert.assertTrue(bordereauResult.isValid());
        claimService.saveObjectForXMLUploader(claimResults.get(0));
        Claim c = claimResults.get(0).getClaim();
        c.AddNotification(new HireUpdatedNotification());
        claimService.updateClaim(c);

        Claim savedClaim = claimService.getClaim(c.getId());
        Assert.assertTrue(savedClaim.getIsIsAnomalies());

    }

    @Test
    @Transactional
    public void testCanTriggerEcdUpdatedNotification() throws IOException {

        String fileName = "andy.20090825.1test.xml";
        String path = "/chox/testFile/andy.20090825.1test.xml";
        File file = new ClassPathResource(path).getFile();
        Assert.assertNotNull(file);

        BordereauResult bordereauResult = uploadClaimXMLService.processBordereau(file, fileName);
        List<ClaimResult> claimResults = bordereauResult.getClaimResult();
        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Assert.assertTrue(bordereauResult.isValid());
        claimService.saveObjectForXMLUploader(claimResults.get(0));
        Claim c = claimResults.get(0).getClaim();
        c.AddNotification(new EcdUpdatedNotification());
        claimService.updateClaim(c);

        Claim savedClaim = claimService.getClaim(c.getId());
        Assert.assertTrue(savedClaim.getIsIsAnomalies());

    }

    @Test
    @Transactional
    public void testCanDetectAnomalous() throws IOException {

        String fileName = "andy.20090825.1test.xml";
        String path = "/chox/testFile/andy.20090825.1test.xml";
        File file = new ClassPathResource(path).getFile();
        Assert.assertNotNull(file);

        BordereauResult bordereauResult = uploadClaimXMLService.processBordereau(file, fileName);
        List<ClaimResult> claimResults = bordereauResult.getClaimResult();
        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Assert.assertTrue(bordereauResult.isValid());
        claimService.saveObjectForXMLUploader(claimResults.get(0));
        Claim c = claimResults.get(0).getClaim();

        //make sure the testing claim have correct policy contact date
        Assert.assertEquals(2009,DateHelper.getYear(c.getPolicyHolderContactDate()));
        Assert.assertEquals(9,DateHelper.getMonth(c.getPolicyHolderContactDate()));
        Assert.assertEquals(15,DateHelper.getDate(c.getPolicyHolderContactDate()));

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
        c.AddNotifications(newECDAddedChecker.getAnomalousNotifications(c));
        Assert.assertTrue(c.getIsIsAnomalies());
        Assert.assertEquals(1, c.getNotifications().size());

        //if the new ECD is 15-10-2009
        //delayed duration = 15 days (Compare to first ecd)
        //so this is a caim with anomalous ECD, if tha claim already conatains a notification with same type
        //if shouldn't add a new notification again
        HireMonitoringEcd newEcd2 = new HireMonitoringEcd();
        newEcd2.setEcdDate(DateHelper.Parse("12/10/2009"));
        c.addHireMonitoringEcd(newEcd2);
        c.AddNotifications(newECDAddedChecker.getAnomalousNotifications(c));
        Assert.assertTrue(c.getIsIsAnomalies());
        Assert.assertEquals(1, c.getNotifications().size());

        //if repair booked in date is friday
        Date friday = DateHelper.Parse("18/09/2009");
        c.getHireMonitoringDetail().setRepairBookInDate(friday);
        c.AddNotifications(hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(c));
        Assert.assertTrue(c.getIsIsAnomalies());
        Assert.assertEquals(2, c.getNotifications().size());


        //Test make sure the notification saved correctly
        claimService.updateClaim(c);

        Claim savedClaim = claimService.getClaim(c.getId());
        Assert.assertTrue(savedClaim.getIsIsAnomalies());
        Assert.assertEquals(2,savedClaim.getNotifications().size());

        savedClaim.getNotifications().get(0).getType().equalsIgnoreCase(EcdAnomalousNotification.class.getSimpleName());
        savedClaim.getNotifications().get(1).getType().equalsIgnoreCase(RepairBookedInOnFridayNotification.class.getSimpleName());

    }    
}

