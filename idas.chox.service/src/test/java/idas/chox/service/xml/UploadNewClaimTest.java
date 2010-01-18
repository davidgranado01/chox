/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.xml;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimParseStatus;
import idas.chox.core.xmlValidation.ClaimResult;
import java.io.File;
import java.io.IOException;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author emmanuel
 */
public class UploadNewClaimTest extends BaseXMLUploadClaimTest {

    @Before
    @Transactional
    public void initialize() throws IOException {

        //upload 7 new claims
        String fileName = "UnitTest-NewClaim_Base.xml";
        File testFile = new ClassPathResource(fileName).getFile();
        BordereauResult result = uploadClaimXMLService.processClaimXMLFile(testFile, fileName);
        Assert.assertTrue(result.isValid());
        Assert.assertEquals(7, result.getClaimResult().size());

        for (ClaimResult cr : result.getClaimResult()) {
            Assert.assertEquals(ClaimParseStatus.newClaim, cr.getClaimParseStatus());
        }
    }

    @Test
    @Transactional
    public void updateClaimsTest() throws IOException {

        Claim claim1BeforeUpload = claimService.getClaimByCHOReferenceNumber("UT-CLAIM001");
        String claim1EngineerReportName = claim1BeforeUpload.getEngineerReport().getName();
        String claim1EngineerReportCompany = claim1BeforeUpload.getEngineerReport().getCompany();

        Claim claim2BeforeUpload = claimService.getClaimByCHOReferenceNumber("UT-CLAIM002");
        claim2BeforeUpload.setStatus(ClaimStatus.CLAIM_CLOSED);
        claimService.updateClaim(claim2BeforeUpload);
        //claim status updated to "Claim Close", it should't allow for editing

        String fileName = "UnitTest-NewClaim_01.xml";
        File testFile = new ClassPathResource(fileName).getFile();
        BordereauResult result = uploadClaimXMLService.processClaimXMLFile(testFile, fileName);

        Assert.assertEquals(3, result.getClaimResult().size());

        //Claim 1 [UT-CLAIM001], exist in CHOX, Detail updated
        Assert.assertEquals(ClaimParseStatus.existClaim, result.getClaimResult().get(0).getClaimParseStatus());
        Assert.assertNotSame(claim1EngineerReportName, result.getClaimResult().get(0).getClaim().getEngineerReport().getName());
        Assert.assertNotSame(claim1EngineerReportCompany, result.getClaimResult().get(0).getClaim().getEngineerReport().getCompany());

        //Claim 2 [UT-CLAIM002], exist in CHOX, but edit not allowed
        Assert.assertEquals(ClaimParseStatus.ClaimNotEditable, result.getClaimResult().get(1).getClaimParseStatus());
  
        //Claim 3, Invalid claim due to mandatory fields not provided
        Assert.assertFalse(result.getClaimResult().get(2).isValid());
    }
}


