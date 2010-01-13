package idas.chox.core.workflow;

import idas.chox.core.model.Claim;

public interface Activity {

    public void setWorkflowContext(WorkflowContext processContext);

    public void process(Claim claim) throws Exception;

    public void processInBatch(Claim claim) throws Exception;

    public void setChainActivity(Activity nextActivity);
}
