package idas.chox.service;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.readers.BordereauReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml","classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-XMLReader-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class ClaimTest {

    @Autowired
    BordereauReader bordereauReader;
    @Autowired
    ClaimService claimService;
    @Autowired
    UploadClaimXMLService service;

    @Test
    @Transactional
    public void testManipulateEcd() throws Exception {
        String fileName = "andy.20090825.1test.xml";
        File file = new ClassPathResource(fileName).getFile();
        Assert.assertNotNull(file);
        
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

        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);


        Claim c = claimResults.get(0).getClaim();

        claimService.updateClaim(claimResults.get(0).getClaim());

        Claim savedClaim = claimService.getClaim(c.getId());

        Assert.assertNotNull(savedClaim);

        HireMonitoringEcd ecd = new HireMonitoringEcd();
        ecd.setEcdDate(DateHelper.getCurrentDate());
        savedClaim.addHireMonitoringEcd(ecd);

        claimService.updateClaim(savedClaim);

        Claim savedClaim2 = claimService.getClaim(c.getId());
        Assert.assertNotNull(savedClaim2);

        Assert.assertEquals(1, savedClaim2.getHireMonitoringEcds().size());
        Assert.assertNotNull(savedClaim2.getLatestHireMonitoringEcd());
    }
}
