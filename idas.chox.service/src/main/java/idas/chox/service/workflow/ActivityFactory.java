package idas.chox.service.workflow;

import idas.chox.core.workflow.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class ActivityFactory implements BeanFactoryAware{

    private BeanFactory beanFactory;
    private WorkflowContext workflowContext;

    public Activity getActivity(String name) {
        Activity activity =   (Activity) beanFactory.getBean(name);
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