package idas.chox.service.workflow.activities;


import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.test.BaseTest;

public class InsurerUploadTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        WebUser webUser = userService.getWebUser(8);
        fakeSecurityInfoProvider.setCurrentUser(webUser);
        fakeSecurityInfoProvider.setIsINS(true);
    }

    @After
    public void tearDownClass() throws Exception {
        fakeSecurityInfoProvider.setIsINS(false);
         
        WebUserRole webUserRole = new WebUserRole();
        webUserRole.setName(WebUserRole.ROLE_CHO_OPR);
        Set roles = new HashSet();
        roles.add(webUserRole);
        
        WebUser currentUser = new WebUser();
        currentUser.setId(999);
        currentUser.setFirstName("UnitTest");
        currentUser.setLastName("User");
        currentUser.setVersion(1);
        currentUser.setRoles(roles);
        

        // SET CHORGANISATION
        Chorganisation chorganisation = new Chorganisation();
        chorganisation.setId(1006);
        chorganisation.setVersion(1);
        currentUser.setChorganisation(chorganisation);
        fakeSecurityInfoProvider.setCurrentUser(currentUser);
    }
    
    @Test(expected = AccessDeniedException.class)
    public void testInsurerUploadWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("insurerUpload");
        activity.process(claim);
    }
    
    @Test
    @Transactional
    public void testInsurerUpload() throws Exception {
        Chorganisation chorganisation = chorganisationService.getChorganisation(1006);
        chorganisation.setInsurerUploadOnly(true);
        Insurer insurer = insurerService.getInsurer(3);
        insurer.setInvoiceUploadEnabled(true);
        List<ClaimResult> claimResults = loadBordereauResult("insurerUpload.xml");
        System.out.println(">>>>> testNewClaim");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();

            claim.setChorganisation(chorganisationService.getChorganisation(1006));
            claim.setInsurer(insurerService.getInsurerByName("RSA"));

            Activity activity = activityFactory.getActivity("insurerUpload");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.MANUAL_INVOICE_APPROVED, claim.getStatus());
        }

    }

    private List<ClaimResult> loadBordereauResult(String fileName) throws Exception {
        File file = new ClassPathResource(fileName).getFile();
        Document document = DocumentHelper.getDocumentFromFile(file);
        List<ClaimResult> claimResults = this.uploadClaimXMLService.formClaimResults(document);
        return claimResults;
    }
}
