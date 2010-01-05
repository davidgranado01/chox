/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.ActivityFactory;
import idas.chox.core.xmlValidation.BordereauResult;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-XMLReader-test.xml"})
public class NewClaimActivityTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    BordereauReader bordereauReader;

    @Test(expected = Exception.class)
    public void testNewClaimWithClaimAlreadyExist() throws Exception {

        Claim claim = new Claim();
        claim.setId(1);//claim with Id = claim already exist in DB
        Activity activity = activityFactory.getActivity("newClaim");
        activity.process(claim);
    }

    @Test
    public void testNewClaim() throws Exception {

        Claim claim = new Claim();
        Activity activity = activityFactory.getActivity("newClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED, claim.getStatus());
    }

    @Test
    @Transactional
    public void testNewClaimWithDoAutoRouting() throws Exception {

        BordereauResult bordereauResult = loadBordereauResult();

        for (ClaimResult claimResult : bordereauResult.getClaimResult()) {
            Claim claim = claimResult.getClaim();
            Activity activity = activityFactory.getActivity("newClaim");
            activity.process(claim);
            Assert.assertEquals(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, claim.getStatus());
            Assert.assertNotNull(claim.getWorkgroup());
        }
    }

    private BordereauResult loadBordereauResult() throws Exception {
        File file = new ClassPathResource("UnitTest-NewClaim_02.xml").getFile();
        BordereauResult bordereauResult = bordereauReader.execute(file);     
        return bordereauResult;
    }
}
