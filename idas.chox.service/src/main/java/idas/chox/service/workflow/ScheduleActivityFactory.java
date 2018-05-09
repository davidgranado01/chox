package idas.chox.service.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import idas.chox.core.workflow.ScheduleActivity;
import idas.chox.core.workflow.WorkflowContext;

public class ScheduleActivityFactory implements BeanFactoryAware {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleActivityFactory.class);

    private BeanFactory beanFactory;
    private WorkflowContext workflowContext;

    public ScheduleActivity getActivity(String name) {
        ScheduleActivity activity = null;
      try {
        LOG.trace("Gettting Activity with Name " + name);
        activity =   (ScheduleActivity) beanFactory.getBean(name);
        LOG.debug("Activity Name " + name + " Activity " +activity.getClass());
//        activity.setWorkflowContext(workflowContext);
      } catch (BeansException ex) {
          LOG.error("Exception creating activity: {}", ex.getMessage(), ex);
          throw(ex);
      }
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