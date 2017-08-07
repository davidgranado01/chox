package idas.chox.service.xml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.test.BaseTest;

/**
 *
 * @author emmanuel
 */
public class UploadNewClaimTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(true);
    }

    @After
    public void tearDownClass() throws Exception {
         fakeSecurityInfoProvider.setIsCHO(false);
    }
    
    @Before
    @Transactional(readOnly = false)
    public void initialize() throws Exception {
        //upload 7 new claims
        String fileName = "UnitTest-NewClaim_Base.xml";
        File testFile = new ClassPathResource(fileName).getFile();
        boolean uploadStatus = uploadClaimXMLService.saveUploadedFile(testFile, fileName);
        Assert.assertTrue(uploadStatus);
        
        Integer id = (bordereauService.getBordereauByFileName(fileName)).getId();
        Assert.assertNotNull(id);
        
        boolean processStatus = uploadClaimXMLService.processFile(id, new HashMap());
        Assert.assertTrue(processStatus); 
    }

    
    @Test
    @Transactional(readOnly = false)
    public void updateClaimsTest() throws Exception {
        
        Claim claim1BeforeUpload = claimService.getClaimByCHOReferenceNumber("CF125341");

        claim1BeforeUpload.setStatus(ClaimStatus.CLAIM_CLOSED);
        claimService.updateClaim(claim1BeforeUpload);

        String fileName = "UnitTest-NewClaim_01.xml";
        File testFile = new ClassPathResource(fileName).getFile();

        Document document = DocumentHelper.getDocumentFromFile(testFile);
        List<ClaimResult> claimResults = this.uploadClaimXMLService.formClaimResults(document);

        Assert.assertEquals(1, claimResults.size());

        //Claim 1 [UT-CLAIM001], exist in CHOX, Detail updated

        for (ClaimResult claimResult : claimResults) {
             bordereauReader.execute(claimResult);
             Claim claim = claimResult.getClaim();
            Assert.assertEquals(ClaimParseStatus.CLAIM_NOT_EDITABLE,claimResult.getClaimParseStatus());
        }
    }
}
