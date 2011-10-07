/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.ClaimRevert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-Workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class ClaimRevertTest {

    @Autowired
    ActivityFactory activityFactory;
    @Autowired
    InsurerService insurerService;
    @Autowired
    AuditTrailService auditTrailService;
    @Autowired
    ClaimService claimService;

    @Test(expected = InvalidClaimStatusException.class)
    public void testClaimRevertWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        Activity activity = activityFactory.getActivity("revertClaim");
        activity.process(claim);
    }

    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void testClaimRevert() throws Throwable {

        Claim claim = new Claim();
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        claim.setChoReference("testing");
        claim.setManagingRepair(false);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        claimService.saveClaimWithoutUpdatingLiabilityPayment(claim);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, claim);
        auditTrailService.logAuditLog(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim);
        ClaimRevert activity = (ClaimRevert) activityFactory.getActivity("revertClaim");
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO, claim.getStatus());
    }
}
