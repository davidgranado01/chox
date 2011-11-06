package idas.chox.service;

import idas.chox.admin.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.Notification;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.notifications.ClaimAnomalousChecker;
import idas.chox.service.notifications.EcdUpdatedNotification;
import idas.chox.service.notifications.HireUpdatedNotification;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

public class NotificationTest extends BaseTest {

    @Autowired
    @Qualifier("newECDAddedChecker")
    ClaimAnomalousChecker newECDAddedChecker;
    @Autowired
    @Qualifier("hireMonitoringDetailUpdatedChecker")
    ClaimAnomalousChecker hireMonitoringDetailUpdatedChecker;
    @Autowired
    UploadClaimXMLService service;

    @Test
    public void canClaimAnomalousCheckerGetInjected() {
        Assert.assertNotNull(newECDAddedChecker);
        Assert.assertNotNull(hireMonitoringDetailUpdatedChecker);
    }

    @Test
    @Transactional
    public void testCanTriggerHireUpdatedNotification() throws Exception {

        String path = "andy.20090825.1test.xml";

        List<ClaimResult> claimResults = loadBordereauResult(path);
        Claim c = claimResults.get(0).getClaim();
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        c.AddNotification(new HireUpdatedNotification());
        Assert.assertTrue(c.getIsIsAnomalies());
    }

    @Test
    @Transactional
    public void testCanTriggerEcdUpdatedNotification() throws Exception {

        String path = "andy.20090825.1test.xml";
        List<ClaimResult> claimResults = loadBordereauResult(path);

        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Claim c = claimResults.get(0).getClaim();
        c.AddNotification(new EcdUpdatedNotification());
        Assert.assertTrue(c.getIsIsAnomalies());

    }

    @Test
    @Transactional
    public void testCanDetectAnomalous() throws Exception {

        String path = "andy.20090825.1test.xml";
        List<ClaimResult> claimResults = loadBordereauResult(path);
        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);


        Claim c = claimResults.get(0).getClaim();

        //make sure the testing claim have correct policy contact date
        Assert.assertEquals(2010, DateHelper.getYear(c.getPolicyHolderContactDate()));
        Assert.assertEquals(9, DateHelper.getMonth(c.getPolicyHolderContactDate()));
        Assert.assertEquals(21, DateHelper.getDate(c.getPolicyHolderContactDate()));

        //policy contect date = 15-09-2009
        //initial ECD = 30-09-2009
        //initial repair duration is 15 days
        //system allow max delayed duration = 50% of initial duration = 7.5 days

        //if the new ECD is 01-10-2009
        //delayed duration = 10 days (Compare to first ecd)
        //so this is a caim with anomalous ECD
        HireMonitoringEcd newEcd = new HireMonitoringEcd();
        newEcd.setEcdDate(DateHelper.Parse("28/09/2010"));
        c.addHireMonitoringEcd(newEcd);
        // TODO: CHECK REQUIRED
        for (Notification notification : newECDAddedChecker.getAnomalousNotifications(c)) {
            c.AddNotification(notification);
        }
        Assert.assertFalse(c.getIsIsAnomalies());
//        Assert.assertEquals(1, c.getNotifications().size());
//
//        //if the new ECD is 15-10-2009
//        //delayed duration = 15 days (Compare to first ecd)
//        //so this is a caim with anomalous ECD, if tha claim already conatains a notification with same type
//        //if shouldn't add a new notification again
//        HireMonitoringEcd newEcd2 = new HireMonitoringEcd();
//        newEcd2.setEcdDate(DateHelper.Parse("12/10/2009"));
//        c.addHireMonitoringEcd(newEcd2);
//        // TODO: CHECK REQUIRED
//        // c.AddNotifications(newECDAddedChecker.getAnomalousNotifications(c));
//        Assert.assertTrue(c.getIsIsAnomalies());
//        Assert.assertEquals(1, c.getNotifications().size());
//
//        //if repair booked in date is friday
//        Date friday = DateHelper.Parse("18/09/2009");
//        c.getHireMonitoringDetail().setRepairBookInDate(friday);
//        // TODO: CHECK REQUIRED
//        // c.AddNotifications(hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(c));
//        for (Notification notification : hireMonitoringDetailUpdatedChecker.getAnomalousNotifications(c)) {
//            c.AddNotification(notification);
//        }
//        Assert.assertTrue(c.getIsIsAnomalies());
//        Assert.assertEquals(2, c.getNotifications().size());
//
//
//        //Test make sure the notification saved correctly
//        claimService.updateClaim(c);
//
//        Claim savedClaim = claimService.getClaim(c.getId());
//        Assert.assertTrue(savedClaim.getIsIsAnomalies());
//        Assert.assertEquals(2, savedClaim.getNotifications().size());
//
//        savedClaim.getNotifications().get(0).getType().equalsIgnoreCase(EcdAnomalousNotification.class.getSimpleName());
//        savedClaim.getNotifications().get(1).getType().equalsIgnoreCase(RepairBookedInOnFridayNotification.class.getSimpleName());

    }

    private List<ClaimResult> loadBordereauResult(String path) throws Exception {
        File file = new ClassPathResource(path).getFile();
        int totalProcessed = 0;
        List<ClaimResult> claimResults = null;
        List<String> choReferences = new ArrayList<String>();
        Document document = DocumentHelper.getDocumentFromFile(file);
        claimResults = this.service.formClaimResults(document);
        for (ClaimResult claimResult : claimResults) {
            if (this.service.doProcessBordereauResult(claimResult, choReferences)) {

                totalProcessed++;

            }
        }
        return claimResults;
    }
}
