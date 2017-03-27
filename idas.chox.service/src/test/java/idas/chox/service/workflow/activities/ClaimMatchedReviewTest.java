package idas.chox.service.workflow.activities;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Claim;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.WebUser;
import idas.chox.core.workflow.Activity;
import org.junit.Before;
import org.junit.Test;

import idas.chox.test.BaseTest;
import java.math.BigDecimal;
import junit.framework.Assert;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author john
 */
public class ClaimMatchedReviewTest extends BaseTest {
    @Before
    public void setUpClass() throws Exception {
        WebUser webUser = userService.getWebUser(8);
        fakeSecurityInfoProvider.setCurrentUser(webUser);
        fakeSecurityInfoProvider.setIsINS(true);
    }

    @Test(expected = Exception.class)
    @Transactional
    public void testNotMatched() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        
        Activity activity = activityFactory.getActivity("claimMatchedReview");
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("Claim was not matched and auto-acknowledged so cannot be reviewed", ex.getMessage());
            throw ex;
        }
    }
    
    @Test(expected = Exception.class)
    @Transactional
    public void testAlreadyMatched() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        claim.setMatchStatus(4);
        Activity activity = activityFactory.getActivity("claimMatchedReview");
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("Claim match has already been reviewed", ex.getMessage());
            throw ex;
        }
    }
    
    @Test(expected = Exception.class)
    @Transactional
    public void testNoWorkgroup() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        claim.setMatchStatus(3);
        
        Activity activity = activityFactory.getActivity("claimMatchedReview");
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("No workgroup selected", ex.getMessage());
            throw ex;
        }
    }
    
    @Test(expected = Exception.class)
    @Transactional
    public void testInvalidWorkgroup() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        claim.setMatchStatus(3);
        ClaimMatchedReview activity = (ClaimMatchedReview) activityFactory.getActivity("claimMatchedReview");
        activity.setMatchedWorkgroupIdField(666);
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("Invalid workgroup", ex.getMessage());
            throw ex;
        }
    }
    
    @Test(expected = Exception.class)
    @Transactional
    public void testNoOwner() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        claim.setMatchStatus(3);
        ClaimMatchedReview activity = (ClaimMatchedReview) activityFactory.getActivity("claimMatchedReview");
        activity.setMatchedWorkgroupIdField(30);
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("No Claim Owner selected", ex.getMessage());
            throw ex;
        }
    }
    
    @Test(expected = Exception.class)
    @Transactional
    public void testInvalidOwner() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        claim.setMatchStatus(3);
        ClaimMatchedReview activity = (ClaimMatchedReview) activityFactory.getActivity("claimMatchedReview");
        activity.setMatchedWorkgroupIdField(30);
        activity.setMatchedClaimOwnerIdField(666);
        try {
            activity.process(claim);
        } catch (Exception ex) {
            Assert.assertEquals("Invalid Claim Owner", ex.getMessage());
            throw ex;
        }
    }
    
    @Test
    @Transactional
    public void testClaimMatchReviewed() throws Exception {
        Claim claim = claimService.getClaim(999);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);

        Chorganisation chorg = chorganisationService.getChorganisation(1007);
        claim.setChorganisation(chorg);
        claim.getInsurer().setEnableClaimMatching(true);
        claim.setBreBand(breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId()));
        claim.getBreBand().setClaimMatchingEnable(true);
        claim.setMatchStatus(3);
        ClaimMatchedReview activity = (ClaimMatchedReview) activityFactory.getActivity("claimMatchedReview");
        activity.setMatchedWorkgroupIdField(30);
        activity.setMatchedClaimOwnerIdField(3);
        activity.setReserveValue(BigDecimal.TEN);
        activity.process(claim);

        Assert.assertEquals(4, claim.getMatchStatus());
        Assert.assertEquals(BigDecimal.TEN, claim.getIndemnityAmount());
    }

}
