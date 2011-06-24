/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
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
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-XMLReader-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class NewClaimActivityTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    BordereauReader bordereauReader;
    @Autowired
    UploadClaimXMLService service;
    

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
            claim.getInsurer().setAutoRoutingEnable(false);
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
            claim.getInsurer().setAutoRoutingEnable(false);
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
            
            claim.getInsurer().setAutoRoutingEnable(true);
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
            claim.getInsurer().setAutoRoutingEnable(true);
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
            claim.getInsurer().setAutoRoutingEnable(true);
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
            claim.getInsurer().setAutoRoutingEnable(true);
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
            claim.getInsurer().setAutoRoutingEnable(true);
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
            claim.getInsurer().setAutoRoutingEnable(false);
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
            claim.getInsurer().setAutoRoutingEnable(false);
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
        List<ClaimResult> claimResults = null;
        List<String> choReferences = new ArrayList<String>();
        File file = new ClassPathResource("UnitTest-NewClaim_Base.xml").getFile();
        Document document = DocumentHelper.getDocumentFromFile(file);
        claimResults = this.service.formClaimResults(document);
//        for (ClaimResult claimResult : claimResults) {
//            if (this.service.doProcessBordereauResult(claimResult, choReferences)) {
//
//                totalProcessed++;
//
//            }
//        }

        return claimResults;
    }
}
