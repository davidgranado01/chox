/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.workflow.Activity;
import idas.chox.core.workflow.exceptions.InvalidClaimStatusException;
import idas.chox.service.workflow.activities.InvoicePaymentReceived;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml", "classpath:applicationContext-BRE-test.xml"})
public class InvoicePaymentReceivedTest {

    @Autowired
    ActivityFactory activityFactory;
   

    @Test(expected = InvalidClaimStatusException.class)
    public void testInvoicePaymentReceivedWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Activity activity = activityFactory.getActivity("invoicePaymentReceived");
        activity.process(claim);
    }

    @Test
    public void testInvoicePaymentReceived() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.INVOICE_PAYMENT_LOGGED);
        InvoicePaymentReceived activity = (InvoicePaymentReceived) activityFactory.getActivity("invoicePaymentReceived");

        
        activity.process(claim);
        Assert.assertEquals(ClaimStatus.INVOICE_PAYMENT_RECEIVED, claim.getStatus());
    }
}
