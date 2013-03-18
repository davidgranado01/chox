package idas.chox.service.workflow.activities;

import idas.chox.test.BaseTest;
import junit.framework.Assert;
import org.junit.Test;

public class WorkFlowContextTest extends BaseTest{

    @Test
    public void testClassInjection() {

        Assert.assertNotNull(activityFactory);
        Assert.assertNotNull(activityFactory.getWorkflowContext());
        Assert.assertNotNull(activityFactory.getWorkflowContext().getDataService());
        Assert.assertNotNull(activityFactory.getWorkflowContext().getSecurityInfoProvider());
        Assert.assertNotNull(activityFactory.getWorkflowContext().getBusinessRulesEngService());
        Assert.assertNotNull(activityFactory.getWorkflowContext().getBusinessRulesEngService().getRulesEngine());
        Assert.assertNotNull(activityFactory.getWorkflowContext().getBusinessRulesEngService().getRulesEngine().getBusinessRules());
        Assert.assertFalse(activityFactory.getWorkflowContext().getBusinessRulesEngService().getRulesEngine().getBusinessRules().isEmpty());
        Assert.assertNotNull(activityFactory.getActivity("newClaim"));
        Assert.assertNotNull(activityFactory.getActivity("assignOwner"));
        Assert.assertNotNull(activityFactory.getActivity("assignWorkgroup"));
        Assert.assertNotNull(activityFactory.getActivity("acknowledgeClaim"));
    }
}
