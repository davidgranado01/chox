package idas.chox.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import junit.framework.Assert;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.services.NotificationService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.data.notifications.EcdUpdatedNotification;
import idas.chox.data.notifications.HireUpdatedNotification;

public class NotificationTest extends BaseTest {

    @Autowired
    UploadClaimXMLService service;
    @Autowired
    NotificationService notificationService;

    @Before
    public void setUpClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(true);
    }

    @After
    public void tearDownClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(false);
    }
    
    @Test
    @Transactional
    public void testCanTriggerHireUpdatedNotification() throws Exception {

        String path = "andy.20090825.1test.xml";

        List<ClaimResult> claimResults = loadBordereauResult(path);
        Claim c = claimResults.get(0).getClaim();
        Assert.assertNotNull(claimResults);
        Assert.assertTrue(claimResults.size() > 0);
        notificationService.addNotification(c, new HireUpdatedNotification());
        
        // TODO : check claim is anomalous
    }

    @Test
    @Transactional
    public void testCanTriggerEcdUpdatedNotification() throws Exception {

        String path = "andy.20090825.1test.xml";
        List<ClaimResult> claimResults = loadBordereauResult(path);

        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Claim c = claimResults.get(0).getClaim();
        notificationService.addNotification(c, new EcdUpdatedNotification());
        
        // TODO : check claim is anomalous

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

        // TODO : check claim is anomalous
    }

    private List<ClaimResult> loadBordereauResult(String path) throws Exception {
        File file = new ClassPathResource(path).getFile();
        int totalProcessed = 0;
        List<String> choReferences = new ArrayList<String>();
        Document document = DocumentHelper.getDocumentFromFile(file);
        List<ClaimResult> claimResults = this.service.formClaimResults(document);
        for (ClaimResult claimResult : claimResults) {
            if (this.service.doProcessBordereauResult(claimResult, choReferences)) {

                totalProcessed++;

            }
        }
        return claimResults;
    }
}
