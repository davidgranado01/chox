package idas.chox.service.workflow;

import idas.chox.test.BaseTest;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.core.xmlValidation.ClaimResult;
import java.io.File;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import idas.chox.core.util.DocumentHelper;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

public class SupplementaryInvoiceActivityTest extends BaseTest{

    @Test
    @Transactional
    public void testNewSupplementaryInvoice() throws Exception {
        List<ClaimResult> claimResults = loadBordereauResult("sstestclaim.xml");
        System.out.println(">>>>> testNewClaim");
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

            claimResults = loadBordereauResult("sstestclaim.xml");
            for (ClaimResult claimResult1 : claimResults) {
                bordereauReader.execute(claimResult1);
                claimResult1.getClaim().setInvoice(claimResult1.getInvoice());
                claim = claimResult1.getClaim();

                activity = activityFactory.getActivity("newInvoice");
                activity.processInBatch(claim);
            }

            Assert.assertEquals(ClaimStatus.INVOICE_ESCALATED_TO_CH, claim.getStatus());

        }

        claimResults = loadBordereauResult("sstestsupplementaryclaim.xml");
        System.out.println(">>>>> testingSupplementaryClaim");
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
        List<ClaimResult> claimResults = null;
        File file = new ClassPathResource(fileName).getFile();
        Document document = DocumentHelper.getDocumentFromFile(file);
        claimResults = this.uploadClaimXMLService.formClaimResults(document);
        return claimResults;
    }
}
