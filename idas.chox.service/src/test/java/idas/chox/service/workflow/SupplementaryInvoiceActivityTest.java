package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.readers.BordereauReader;
import java.io.File;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import idas.chox.core.util.DocumentHelper;
import java.util.List;
import org.w3c.dom.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-XMLReader-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class SupplementaryInvoiceActivityTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    BordereauReader bordereauReader;
    @Autowired
    UploadClaimXMLService service;
    @Autowired
    InsurerService insurerService;
    @Autowired
    ChorganisationService chorganisationService;

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

            Assert.assertEquals(ClaimStatus.INVOICE_ESCALATED, claim.getStatus());

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
        claimResults = this.service.formClaimResults(document);
        return claimResults;
    }
}
