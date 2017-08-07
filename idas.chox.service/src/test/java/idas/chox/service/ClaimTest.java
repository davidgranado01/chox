package idas.chox.service;

import java.io.File;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.DateHelper;
import idas.chox.test.BaseTest;

public class ClaimTest extends BaseTest {

    @Autowired
    UploadClaimXMLService service;

    @Test
    @Transactional
    public void testManipulateEcd() throws Exception {
        String fileName = "andy.20090825.1test.xml";
        File testFile = new ClassPathResource(fileName).getFile();
        boolean uploadStatus = uploadClaimXMLService.saveUploadedFile(testFile, fileName);
        junit.framework.Assert.assertTrue(uploadStatus);
        
        Integer id = (bordereauService.getBordereauByFileName(fileName)).getId();
        junit.framework.Assert.assertNotNull(id);
        
        boolean processStatus = uploadClaimXMLService.processFile(id, new HashMap());
        junit.framework.Assert.assertTrue(processStatus); 


        Claim savedClaim = claimService.getClaimByCHOReferenceNumber("CF125341");

        Assert.assertNotNull(savedClaim);

        HireMonitoringEcd ecd = new HireMonitoringEcd();
        ecd.setEcdDate(DateHelper.getCurrentDate());
        savedClaim.addHireMonitoringEcd(ecd);
        claimService.updateClaim(savedClaim);
        Claim savedClaim2 = claimService.getClaim(savedClaim.getId());

        Assert.assertNotNull(savedClaim2);
        Assert.assertEquals(1, savedClaim2.getHireMonitoringEcds().size());
        Assert.assertNotNull(savedClaim2.getLatestHireMonitoringEcd());
    }
    
    @Before
    public void setUpClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(true);
    }

    @After
    public void tearDownClass() throws Exception {
         fakeSecurityInfoProvider.setIsCHO(false);
    }
}
