/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.workflow.ActivityFactory;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-workflow-test.xml", "classpath:applicationContext-test.xml", "classpath:applicationContext-services-test.xml"})
public class WorkFlowContextTest {

    @Autowired
    ActivityFactory activityFactory;

    @Test
    public void testClassInjection() {

        Assert.assertNotNull(activityFactory);
        Assert.assertNotNull(activityFactory.getWorkflowContext());
        Assert.assertNotNull(activityFactory.getWorkflowContext().getDataService());
        Assert.assertNotNull(activityFactory.getWorkflowContext().getSecurityInfoProvider());
        Assert.assertNotNull(activityFactory.getWorkflowContext().getBusinessRulesEngService());
        Assert.assertNotNull(activityFactory.getActivity("newClaim"));
        Assert.assertNotNull(activityFactory.getActivity("assignOwner"));
        Assert.assertNotNull(activityFactory.getActivity("assignWorkGroup"));
        Assert.assertNotNull(activityFactory.getActivity("acknowledgeClaim"));
    }
}
