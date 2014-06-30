package idas.chox.service.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import idas.chox.core.workflow.*;

public class ActivityFactory implements BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(ActivityFactory.class);

    private BeanFactory beanFactory;
    private WorkflowContext workflowContext;

    public Activity getActivity(String name) {
        Activity activity =   (Activity) beanFactory.getBean(name);
        LOG.debug("Activity Name " + name + " Activity " +activity.getClass());
        activity.setWorkflowContext(workflowContext);
        return activity;
    }

    public void setWorkflowContext(WorkflowContext processContext) {
        this.workflowContext = processContext;
    }

    public WorkflowContext getWorkflowContext() {
        return this.workflowContext;
    }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}