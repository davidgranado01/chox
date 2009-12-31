/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow;

import idas.chox.core.services.DataService;
import idas.chox.core.workflow.WorkflowContext;

/**
 *
 * @author emmanuel
 */
public class ClaimProcessWorkflowContext implements WorkflowContext {

    private DataService dataService;

    @Override
    public DataService getDataService() {
        return dataService;
    }

    @Override
    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
}
