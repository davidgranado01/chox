/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.readers.BordereauReader;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-XMLReader-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class ClaimTest {

    @Autowired
    BordereauReader bordereauReader;
    @Autowired
    ClaimService claimService;


    @Test
    @Transactional
    public void testManipulateEcd() throws Exception {
        String fileName = "andy.20090825.1test.xml";
        File file = new ClassPathResource(fileName).getFile();
        Assert.assertNotNull(file);

        BordereauResult bordereauResult = null; //bordereauReader.execute(file);
        List<ClaimResult> claimResults = bordereauResult.getClaimResult();
        Assert.assertNotNull(claimResults);

        Assert.assertTrue(claimResults.size() > 0);

        Assert.assertTrue(bordereauResult.isValid());

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

