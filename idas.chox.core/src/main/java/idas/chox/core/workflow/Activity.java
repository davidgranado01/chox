/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.workflow;

import idas.chox.core.model.Claim;

/**
 *
 * @author emmanuel
 */
public interface Activity {

    public void setProcessContext(WorkflowContext processContext);

    public void process(Claim claim) throws Exception;

    public void processInBatch(Claim claim) throws Exception;
}
