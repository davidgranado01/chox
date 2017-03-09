package idas.chox.service.workflow.activities;

import java.io.File;
import java.util.List;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.test.BaseTest;

public class SupplementaryInvoiceTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        List<ClaimResult> claimResults = loadBordereauResult("sstestclaim.xml");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();

            claim.setChorganisation(chorganisationService.getChorganisation(1006));
            claim.setInsurer(insurerService.getInsurerByName("RSA"));

            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, claim.getStatus());
            claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
            activity = activityFactory.getActivity("awaitingCarHireInfo");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, claim.getStatus());
            claimService.save(claim);
            claimService.flush();
        }

        List<ClaimResult> claimResults2 = loadBordereauResult("sstestclaim.xml");
        for (ClaimResult claimResult2 : claimResults2) {
            bordereauReader.execute(claimResult2);
            claimResult2.getClaim().setInvoice(claimResult2.getInvoice());
            Claim claim = claimResult2.getClaim();

            System.out.println("....processing newInvoice activity on claim '" + claim.getChoReference() + "' in status " + claim.getStatus() + "....");
            Activity activity = activityFactory.getActivity("newInvoice");
            activity.processInBatch(claim);
            claimService.save(claim);
//            claimService.flush();
            Assert.assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, claim.getStatus());
        }
    }

    @Test
    @Transactional
    public void testNewSupplementaryInvoice() throws Exception {

        List<ClaimResult> claimResults = loadBordereauResult("sstestsupplementaryclaim.xml");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();
            claim.setInvoice(claimResult.getInvoice());

            Activity activity = activityFactory.getActivity("supplementaryInvoice");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, claim.getStatus());
        }
    }

    private List<ClaimResult> loadBordereauResult(String fileName) throws Exception {
        List<ClaimResult> claimResults;
        File file = new ClassPathResource(fileName).getFile();
        Document document = DocumentHelper.getDocumentFromFile(file);
        claimResults = this.uploadClaimXMLService.formClaimResults(document);
        return claimResults;
    }
}
