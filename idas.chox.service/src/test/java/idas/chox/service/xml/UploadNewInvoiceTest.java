package idas.chox.service.xml;

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
public class UploadNewInvoiceTest extends BaseTest {
    
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

        List<String> choReferences = new ArrayList<String>();
        //upload 7 new claims
        String fileName = "UnitTest-NewClaim_Base.xml";
        File testFile = new ClassPathResource(fileName).getFile();
        Document document = DocumentHelper.getDocumentFromFile(testFile);
        List<ClaimResult> claimResults = uploadClaimXMLService.formClaimResults(document);
        Assert.assertEquals(1, claimResults.size());

        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Assert.assertEquals(ClaimParseStatus.NEW_CLAIM, claimResult.getClaimParseStatus());
            uploadClaimXMLService.doProcessBordereauResult(claimResult, choReferences);
        }

    }

    @Test
    @Transactional
    public void updateInvoiceTest() throws Exception {

        Claim claim1BeforeUpload = claimService.getClaimByCHOReferenceNumber("CF125341");
        claim1BeforeUpload.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        claimService.updateClaim(claim1BeforeUpload);


        String fileName = "UnitTest-NewClaim_01.xml";
        File testFile = new ClassPathResource(fileName).getFile();
        List<String> choReferences = new ArrayList<String>();

        Document document = DocumentHelper.getDocumentFromFile(testFile);
        List<ClaimResult> claimResults = uploadClaimXMLService.formClaimResults(document);
        Assert.assertEquals(1, claimResults.size());

        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Assert.assertEquals(ClaimParseStatus.NEW_INVOICE, claimResult.getClaimParseStatus());
        }
    }
}
