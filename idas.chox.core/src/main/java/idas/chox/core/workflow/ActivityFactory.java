/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.workflow;

import java.util.Map;

/**
 *
 * @author emmanuel
 */
public class ActivityFactory {

    private Map<String, Activity> activities;
    private WorkflowContext workflowContext;

    public Activity getActivity(String name) {
        Activity activity = activities.get(name);
        activity.setProcessContext(workflowContext);
        return activity;
    }

    public void setWorkflowContext(WorkflowContext processContext) {
        this.workflowContext = processContext;
    }

    public WorkflowContext getWorkflowContext() {
        return this.workflowContext;
    }

    public void setActivities(Map<String, Activity> activities) {
        this.activities = activities;
    }
}
