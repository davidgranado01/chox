package idas.chox.service.workflow.activities;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Chorganisation;
import java.io.File;
import java.util.List;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingBand;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.workflow.Activity;
import idas.chox.core.xmlValidation.ClaimResult;

import idas.chox.test.BaseTest;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author john
 */
public class ClaimMatchingTest extends BaseTest {

    @Before
    public void setUpClass() throws Exception {
        WebUser webUser = userService.getWebUser(2);
        fakeSecurityInfoProvider.setCurrentUser(webUser);
        fakeSecurityInfoProvider.setIsINS(false);
// get claim, set insurer, cho, bre_band
//  activate claim matching on insurer, in breband
//    set up bre band correctly...
// .... maybe get configuredclaim from db
//    get claim EA059943 999

//        List<ClaimResult> claimResults = loadBordereauResult("sstestclaim.xml");
//        for (ClaimResult claimResult : claimResults) {
//            bordereauReader.execute(claimResult);
//            Claim claim = claimResult.getClaim();
//
//            claim.setChorganisation(chorganisationService.getChorganisation(1006));
//            claim.setInsurer(insurerService.getInsurerByName("RSA"));
//
//            Activity activity = activityFactory.getActivity("newClaim");
//            activity.process(claim);
//            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, claim.getStatus());
//            claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
//            activity = activityFactory.getActivity("awaitingCarHireInfo");
//            activity.process(claim);
//            Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, claim.getStatus());
//            claimService.save(claim);
//            claimService.flush();
//        }
    }

    @Test(expected = Exception.class)
    @Transactional
    public void testInsurerNotEnabled() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(false);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        
        Activity activity = activityFactory.getActivity("claimMatching");
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("Claim matching not enabled for insurer", ex.getMessage());
            throw ex;
        }
    }
    
    @Test(expected = Exception.class)
    @Transactional
    public void testBreNotEnabled() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(false);
        Activity activity = activityFactory.getActivity("claimMatching");
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("Claim matching not enabled in BreBand", ex.getMessage());
            throw ex;
        }
    }
    
    @Test(expected = Exception.class)
    @Transactional
    public void testNoClaimMatchingBand() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        Activity activity = activityFactory.getActivity("claimMatching");
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("No Claim Matching Band Found", ex.getMessage());
            throw ex;
        }
    }
    
    @Test
    @Transactional
    public void testBasicMatch() throws Exception {
        // Claim number and Indemnity Stance Updated
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        
        claim.getInsurer().setEnableClaimMatching(true);
        BreBand  breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        breBand.setClaimMatchingEnable(true);
        claim.setBreBand(breBand);
//        claim.getBreBand().setClaimMatchingWorkgroup(claimMatchingWorkgroup);
//        claim.getBreBand().setClaimMatchingOwner(claimMatchingOwner);
        claim.getCustomer().setVehicleClass(vehicleClassService.getVehicleClass(94)); // 94 is P2
        claim.setClaimType(ClaimType.GTA);

        ClaimMatching activity = (ClaimMatching) activityFactory.getActivity("claimMatching");
        activity.setClaimMatchingBand(breBand.getClaimMatchingBands().get(0));
        activity.setClaimNumber("updated");
        activity.setIndemnityStance("No Involvement");
        
        activity.process(claim);
        
        Assert.assertEquals("updated", claim.getClaimNumber());
        Assert.assertEquals("No Involvement", claim.getIndemnityStance());
        
    }
    
    @Test
    @Transactional
    public void testFullMatchNoAcknowledge() throws Exception {
        // Claim number, Indemnity Stance and liability Updated
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        
        claim.getInsurer().setEnableClaimMatching(true);
        BreBand  breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        breBand.setClaimMatchingEnable(true);
        claim.setBreBand(breBand);
        breBand.getClaimMatchingBands().get(0).setAutoAcknowledge(false);
//        claim.getBreBand().setClaimMatchingWorkgroup(claimMatchingWorkgroup);
//        claim.getBreBand().setClaimMatchingOwner(claimMatchingOwner);
        claim.getCustomer().setVehicleClass(vehicleClassService.getVehicleClass(94)); // 94 is P2
        claim.setClaimType(ClaimType.GTA);
        String status = claim.getStatus();
        ClaimMatching activity = (ClaimMatching) activityFactory.getActivity("claimMatching");
        activity.setClaimMatchingBand(breBand.getClaimMatchingBands().get(0));
        activity.setClaimNumber("updated");
        activity.setIndemnityStance("No Involvement");
        activity.setLiabilityInsurer(BigDecimal.TEN);
        activity.setLiabilityStance("Insured partially at fault");
        
        activity.process(claim);
        
        Assert.assertEquals("updated", claim.getClaimNumber());
        Assert.assertEquals("No Involvement", claim.getIndemnityStance());
        Assert.assertEquals("Liability Split", claim.getLiabilityStatus().toString());
        Assert.assertEquals(BigDecimal.TEN, claim.getPercentageLiabilityAccepted());
        Assert.assertEquals(status, claim.getStatus());
    }
    
    @Test
    @Transactional
    public void testFullMatchAcknowledged1() throws Exception {
        // Claim number, Indemnity Stance and liability Updated, claim progressed
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        
        claim.getInsurer().setEnableClaimMatching(true);
        BreBand  breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        breBand.setClaimMatchingEnable(true);
        claim.setBreBand(breBand);
//        claim.getBreBand().setClaimMatchingWorkgroup(claimMatchingWorkgroup);
//        claim.getBreBand().setClaimMatchingOwner(claimMatchingOwner);
        claim.getCustomer().setVehicleClass(vehicleClassService.getVehicleClass(94)); // 94 is P2
        claim.setClaimType(ClaimType.GTA);

        ClaimMatching activity = (ClaimMatching) activityFactory.getActivity("claimMatching");
        activity.setClaimMatchingBand(breBand.getClaimMatchingBands().get(0));
        activity.setClaimNumber("updated");
        activity.setIndemnityStance("No Involvement");
        activity.setLiabilityInsurer(BigDecimal.TEN);
        activity.setLiabilityStance("Insured partially at fault");
        
        activity.process(claim);
        
        Assert.assertEquals("updated", claim.getClaimNumber());
        Assert.assertEquals("No Involvement", claim.getIndemnityStance());
        Assert.assertEquals("Liability Split", claim.getLiabilityStatus().toString());
        Assert.assertEquals(BigDecimal.TEN, claim.getPercentageLiabilityAccepted());
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
    }
    
    @Test
    @Transactional
    public void testFullMatchAcknowledged2() throws Exception {
        // Claim number, Indemnity Stance and liability Updated, claim routed and progressed
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        
        claim.getInsurer().setEnableClaimMatching(true);
        BreBand  breBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        breBand.setClaimMatchingEnable(true);
        claim.setBreBand(breBand);
        claim.getBreBand().setClaimMatchingWorkgroup(workgroupService.getWorkgroup(30));
        claim.getBreBand().setClaimMatchingOwner(userService.getWebUser(2));
        claim.getCustomer().setVehicleClass(vehicleClassService.getVehicleClass(94)); // 94 is P2
        claim.setClaimType(ClaimType.GTA);
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claim.setWorkgroup(null);
        claim.setClaimOwner(null);

        ClaimMatching activity = (ClaimMatching) activityFactory.getActivity("claimMatching");
        activity.setClaimMatchingBand(breBand.getClaimMatchingBands().get(0));
        activity.setClaimNumber("updated");
        activity.setIndemnityStance("No Involvement");
        activity.setLiabilityInsurer(BigDecimal.TEN);
        activity.setLiabilityStance("Insured partially at fault");
        
        activity.process(claim);
        
        Assert.assertEquals("updated", claim.getClaimNumber());
        Assert.assertEquals("No Involvement", claim.getIndemnityStance());
        Assert.assertEquals("Liability Split", claim.getLiabilityStatus().toString());
        Assert.assertEquals(BigDecimal.TEN, claim.getPercentageLiabilityAccepted());
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
        Assert.assertEquals(30, claim.getWorkgroup().getId().intValue());
        Assert.assertEquals(2, claim.getClaimOwner().getId().intValue());
    }
    
    @Test
    @Transactional
    public void testFullMatchAcknowledged3() throws Exception {
        // Claim number, Indemnity Stance and liability Updated
    }
    
    @Test
    @Transactional
    public void testFullMatchAcknowledged4() throws Exception {
        // Claim number, Indemnity Stance and liability Updated
    }

    private List<ClaimResult> loadBordereauResult(String fileName) throws Exception {
        File file = new ClassPathResource(fileName).getFile();
        Document document = DocumentHelper.getDocumentFromFile(file);
        List<ClaimResult> claimResults = this.uploadClaimXMLService.formClaimResults(document);
        return claimResults;
    }

}
