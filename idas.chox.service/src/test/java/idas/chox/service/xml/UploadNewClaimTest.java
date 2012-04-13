package idas.chox.service.xml;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

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
    @Transactional
    public void initialize() throws Exception {

        //upload 7 new claims
        String fileName = "UnitTest-NewClaim_Base.xml";
        List<ClaimResult> claimResults = null;
        List<String> choReferences = new ArrayList<String>();
        File testFile = new ClassPathResource(fileName).getFile();
//        BordereauResult result =null; // uploadClaimXMLService.processClaimXMLFile(testFile, fileName);
        Document document = DocumentHelper.getDocumentFromFile(testFile);
        claimResults = this.uploadClaimXMLService.formClaimResults(document);
//        Assert.assertTrue(result.isValid());
        Assert.assertEquals(1, claimResults.size());

        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
//            Claim claim = claimResult.getClaim();
            Assert.assertEquals(ClaimParseStatus.NEW_CLAIM, claimResult.getClaimParseStatus());
            uploadClaimXMLService.doProcessBordereauResult(claimResult, choReferences);
        }
       

    }

    @Test
    @Transactional
    public void updateClaimsTest() throws Exception {
        
      

        List<ClaimResult> claimResults = null;
        Claim claim1BeforeUpload = claimService.getClaimByCHOReferenceNumber("CF125341");
//        String claim1EngineerReportName = claim1BeforeUpload.getEngineerReport().getName();
//        String claim1EngineerReportCompany = claim1BeforeUpload.getEngineerReport().getCompany();

//        Claim claim2BeforeUpload = claimService.getClaimByCHOReferenceNumber("CF125341");
        claim1BeforeUpload.setStatus(ClaimStatus.CLAIM_CLOSED);
        claimService.updateClaim(claim1BeforeUpload);
        //claim status updated to "Claim Close", it should't allow for editing

        String fileName = "UnitTest-NewClaim_01.xml";
        File testFile = new ClassPathResource(fileName).getFile();
//        BordereauResult result = null; //uploadClaimXMLService.processClaimXMLFile(testFile, fileName);

        Document document = DocumentHelper.getDocumentFromFile(testFile);
        claimResults = this.uploadClaimXMLService.formClaimResults(document);

        Assert.assertEquals(1, claimResults.size());

        //Claim 1 [UT-CLAIM001], exist in CHOX, Detail updated

        for (ClaimResult claimResult : claimResults) {
             bordereauReader.execute(claimResult);
             Claim claim = claimResult.getClaim();
            Assert.assertEquals(ClaimParseStatus.CLAIM_NOT_EDITABLE,claimResult.getClaimParseStatus());
//            Assert.assertNotSame(claim1EngineerReportName, claim.getEngineerReport().getName());
//            Assert.assertNotSame(claim1EngineerReportCompany, claim.getEngineerReport().getCompany());

            //Claim 2 [UT-CLAIM002], exist in CHOX, but edit not allowed
//             Assert.assertEquals(ClaimParseStatus.EXIST_CLAIM, claimResult.getClaimParseStatus());

            //Claim 3, Invalid claim due to mandatory fields not provided
//            Assert.assertFalse(result.getClaimResult().get(2).isValid());
        }
    }
}
