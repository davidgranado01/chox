package idas.chox.service.xml;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

/**
 *
 * @author emmanuel
 */
public class UploadNewInvoiceTest extends BaseXMLUploadClaimTest {

    @Before
    @Transactional
    public void initialize() throws Exception {

        List<ClaimResult> claimResults = null;
        List<String> choReferences = new ArrayList<String>();
        //upload 7 new claims
        String fileName = "UnitTest-NewClaim_Base.xml";
        File testFile = new ClassPathResource(fileName).getFile();
//        BordereauResult result = null; //uploadClaimXMLService.processClaimXMLFile(testFile, fileName);
        Document document = DocumentHelper.getDocumentFromFile(testFile);
        claimResults = uploadClaimXMLService.formClaimResults(document);
        Assert.assertEquals(1, claimResults.size());

        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
//            Claim claim = claimResult.getClaim();
            Assert.assertEquals(ClaimParseStatus.newClaim, claimResult.getClaimParseStatus());
            uploadClaimXMLService.doProcessBordereauResult(claimResult, choReferences);
        }

//        Assert.assertTrue(result.isValid());
//        Assert.assertEquals(1, result.getClaimResult().size());

//        for (ClaimResult cr : result.getClaimResult()) {
//            Assert.assertEquals(ClaimParseStatus.newClaim, cr.getClaimParseStatus());
//        }
    }

    @Test
    @Transactional
    public void updateInvoiceTest() throws Exception {

        Claim claim1BeforeUpload = claimService.getClaimByCHOReferenceNumber("CF125341");
//        String claim1EngineerReportName = claim1BeforeUpload.getEngineerReport().getName();
//        String claim1EngineerReportCompany = claim1BeforeUpload.getEngineerReport().getCompany();
        claim1BeforeUpload.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        claimService.updateClaim(claim1BeforeUpload);

//        Claim claim2BeforeUpload = claimService.getClaimByCHOReferenceNumber("UT-CLAIM002");
//        claim2BeforeUpload.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
//        claimService.updateClaim(claim2BeforeUpload);
//
//        Claim claim3BeforeUpload = claimService.getClaimByCHOReferenceNumber("UT-CLAIM003");
//        claim3BeforeUpload.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
//        claimService.updateClaim(claim3BeforeUpload);
//
//        Claim claim4BeforeUpload = claimService.getClaimByCHOReferenceNumber("UT-CLAIM004");
//        claim4BeforeUpload.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
//        claimService.updateClaim(claim4BeforeUpload);

        String fileName = "UnitTest-NewClaim_01.xml";
        File testFile = new ClassPathResource(fileName).getFile();
        List<ClaimResult> claimResults = null;
        List<String> choReferences = new ArrayList<String>();
//        BordereauResult result =null; // uploadClaimXMLService.processClaimXMLFile(testFile, fileName);

        Document document = DocumentHelper.getDocumentFromFile(testFile);
        claimResults = uploadClaimXMLService.formClaimResults(document);
        Assert.assertEquals(1, claimResults.size());

        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
//            Claim claim = claimResult.getClaim();
            Assert.assertEquals(ClaimParseStatus.newInvoice, claimResult.getClaimParseStatus());
//            uploadClaimXMLService.doProcessBordereauResult(claimResult, choReferences);
//            Assert.assertEquals(ClaimParseStatus.newInvoice, result.getClaimResult().get(2).getClaimParseStatus());
        }


//        Assert.assertEquals(4, result.getClaimResult().size());
//
//        //Claim 1 [UT-CLAIM001], exist in CHOX, Detail updated
//        Assert.assertEquals(ClaimParseStatus.existClaim, result.getClaimResult().get(0).getClaimParseStatus());
//        Assert.assertNotSame(claim1EngineerReportName, result.getClaimResult().get(0).getClaim().getEngineerReport().getName());
//        Assert.assertNotSame(claim1EngineerReportCompany, result.getClaimResult().get(0).getClaim().getEngineerReport().getCompany());
//        //Uploading xml have no invoice detaill for this claim, so it's status remain same, only claim details updated
//        Assert.assertNull(result.getClaimResult().get(0).getClaim().getInvoice());
//        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, result.getClaimResult().get(0).getClaim().getStatus());
//
//        //Claim 2 [UT-CLAIM002], exist in CHOX, but invoice update not allowed
//        Assert.assertEquals(ClaimParseStatus.existClaim, result.getClaimResult().get(1).getClaimParseStatus());

        //Claim 3, Invalid claim not updated due to mandatory fields not provided
//        Assert.assertEquals(ClaimParseStatus.newInvoice, result.getClaimResult().get(2).getClaimParseStatus());
//        Assert.assertFalse(result.getClaimResult().get(2).isValid());
//        //status remain same
//        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, result.getClaimResult().get(2).getClaim().getStatus());
//
//        //Claim 4, Invoice update and claim status promoted to "InvoiceApprovedByBre"
//        Assert.assertEquals(ClaimParseStatus.newInvoice, result.getClaimResult().get(3).getClaimParseStatus());
//        Assert.assertTrue(result.getClaimResult().get(3).isValid());
//        Assert.assertNotNull(result.getClaimResult().get(3).getClaim().getInvoice());
//        Assert.assertEquals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT, result.getClaimResult().get(3).getClaim().getStatus());

    }
}
