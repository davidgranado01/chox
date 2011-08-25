package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.InvoiceAccepted;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class InvoiceAcceptedTest {

    @Autowired
    ActivityFactory activityFactory;
   

    @Test(expected = InvalidClaimStatusException.class)
    public void testInvoiceAcceptedWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("acceptInvoice");
        activity.process(claim);
    }

    @Test
    public void testInvoiceAccepted() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        InvoiceAccepted activity = (InvoiceAccepted) activityFactory.getActivity("acceptInvoice");

        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.AWAITING_INVOICE_PAYMENT, claim.getStatus());
    }
}
