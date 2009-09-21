/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import chox.Util.DateHelper;
import chox.services.ClaimService;
import chox.services.UploadClaimXMLService;
import chox.xmlValidation.model.BordereauResult;
import chox.services.ClaimResult;
import java.io.File;
import java.io.IOException;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Emmanuel
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-services.xml"})
public class ClaimTest {

    @Autowired
    UploadClaimXMLService uploadClaimXMLService;
    @Autowired
    ClaimService claimService;

    @Test
    @Transactional
    public void testManipulateEcd() throws IOException {
        String fileName = "andy.20090825.1test.xml";
        String path = "/chox/testFile/andy.20090825.1test.xml";
        File file = new ClassPathResource(path).getFile();
        Assert.assertNotNull(file);

        BordereauResult bordereauResult = uploadClaimXMLService.processBordereau(file, fileName);
        List<ClaimResult> claimResults = bordereauResult.getClaimResult();
        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Assert.assertTrue(bordereauResult.isValid());

        Claim c = claimResults.get(0).getClaim();

        claimService.saveObjectForXMLUploader(claimResults.get(0));

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
//    @Test
//    public void ExportSchema()
//    {
//        SchemaExport schemaExport = new SchemaExport(sessionFactory.getConfiguration());
//        schemaExport.create(true, false);
//    }
}

