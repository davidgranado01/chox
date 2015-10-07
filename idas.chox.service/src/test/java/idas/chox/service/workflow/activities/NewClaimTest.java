package idas.chox.service.workflow.activities;

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

import idas.chox.core.model.AutomaticRoutingStrategy;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.test.BaseTest;

public class NewClaimTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        fakeSecurityInfoProvider.setIsCHO(true);
    }

    @After
    public void tearDownClass() throws Exception {
         fakeSecurityInfoProvider.setIsCHO(false);
    }

    @Test(expected = Exception.class)
    public void testNewClaimWithClaimAlreadyExist() throws Exception {

        Claim claim = new Claim();
        claim.setId(1);//claim with Id = claim already exist in DB
        Activity activity = activityFactory.getActivity("newClaim");
        activity.process(claim);
    }

    @Test
    @Transactional
    public void testNewClaim1() throws Exception {
        
        //Workgroup Feature  : false
        //Auto Routing       : false
        //Ownership Feauture : false
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim1");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();

            claim.getInsurer().setWorkgroupEnable(false);
            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.NONE);
            claim.getInsurer().setClaimOwnershipEnable(false);
            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
            Assert.assertNull(claim.getWorkgroup());
        }
    }

    @Test
    @Transactional
    public void testNewClaim2() throws Exception {
        //Workgroup Feature  : true
        //Auto Routing       : false
        //Ownership Feauture : false
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim2");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();

            claim.getInsurer().setWorkgroupEnable(true);
            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.NONE);
            claim.getInsurer().setClaimOwnershipEnable(false);

            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, claim.getStatus());
            Assert.assertNull(claim.getWorkgroup());
        }
    }

    @Test
    @Transactional
    public void testNewClaim3_PolicyNumberPassed() throws Exception {
        //Workgroup Feature  : true
        //Auto Routing       : true
        //Ownership Feauture : false
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim3_PolicyNumberPassed");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();

            claim.getInsurer().setWorkgroupEnable(true);

            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.POLICY);
            claim.getInsurer().setClaimOwnershipEnable(false);

            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim.getStatus());
            Assert.assertNotNull(claim.getWorkgroup());
        }
    }

    @Test
    @Transactional
    public void testNewClaim3_PolicyNumberFailed() throws Exception {
        //Workgroup Feature  : true
        //Auto Routing       : true
        //Ownership Feauture : false
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim3_PolicyNumberFailed");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();

            claim.getInsurer().setWorkgroupEnable(true);
            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.POLICY);
            claim.getInsurer().setClaimOwnershipEnable(false);
            claim.getThirdParty().setPolicyNumber("**ABC***");

            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, claim.getStatus());
            Assert.assertNull(claim.getWorkgroup());
        }
    }

    @Test
    @Transactional
    public void testNewClaim4_PolicyNumberPassed() throws Exception {
        //Workgroup Feature  : true
        //Auto Routing       : true
        //Ownership Feauture : true
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim4_PolicyNumberPassed");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();

            claim.getInsurer().setWorkgroupEnable(true);
            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.POLICY);
            claim.getInsurer().setClaimOwnershipEnable(true);

            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
            Assert.assertNotNull(claim.getWorkgroup());
        }
    }

    @Test
    @Transactional
    public void testNewClaim4_PolicyNumberFailed() throws Exception {
        //Workgroup Feature  : true
        //Auto Routing       : true
        //Ownership Feauture : true
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim4_PolicyNumberFailed");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();

            claim.getInsurer().setWorkgroupEnable(true);
            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.POLICY);
            claim.getInsurer().setClaimOwnershipEnable(true);
            claim.getThirdParty().setPolicyNumber("**ABC***");

            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, claim.getStatus());
            Assert.assertNull(claim.getWorkgroup());
        }
    }

    @Test
    @Transactional
    public void testNewClaim5() throws Exception {
        //Workgroup Feature  : false
        //Auto Routing       : true
        //Ownership Feauture : true
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim5");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();
            claim.getInsurer().setWorkgroupEnable(false);
            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.POLICY);
            claim.getInsurer().setClaimOwnershipEnable(true);
            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
            Assert.assertNull(claim.getWorkgroup());
        }
    }

    @Test
    @Transactional
    public void testNewClaim6() throws Exception {
        //Workgroup Feature  : false
        //Auto Routing       : false
        //Ownership Feauture : true
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim6");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();
            claim.getInsurer().setWorkgroupEnable(false);
            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.NONE);
            claim.getInsurer().setClaimOwnershipEnable(true);
            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
            Assert.assertNull(claim.getWorkgroup());
        }
    }

    @Test
    @Transactional
    public void testNewClaim7() throws Exception {
        //Workgroup Feature  : true
        //Auto Routing       : false
        //Ownership Feauture : true
        List<ClaimResult> claimResults = loadBordereauResult();
        System.out.println(">>>>> testNewClaim7");
        for (ClaimResult claimResult : claimResults) {
            bordereauReader.execute(claimResult);
            Claim claim = claimResult.getClaim();
            claim.getInsurer().setWorkgroupEnable(true);
            claim.getInsurer().setAutomaticRoutingStrategy(AutomaticRoutingStrategy.NONE);
            claim.getInsurer().setClaimOwnershipEnable(true);
            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, claim.getStatus());
            Assert.assertNull(claim.getWorkgroup());
        }
    }

    private List<ClaimResult> loadBordereauResult() throws Exception {
        //This xml clontains one claim
        //This claim have insurer RSA which is Workgroup Feature : true, Ownership Feauture : true, Auto Routing : true by default
        int totalProcessed = 0;
        List<String> choReferences = new ArrayList<>();
        File file = new ClassPathResource("UnitTest-NewClaim_Base.xml").getFile();
        Document document = DocumentHelper.getDocumentFromFile(file);
        List<ClaimResult> claimResults = this.uploadClaimXMLService.formClaimResults(document);

        return claimResults;
    }
}
