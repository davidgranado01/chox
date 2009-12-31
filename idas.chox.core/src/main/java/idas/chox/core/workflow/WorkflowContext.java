/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.workflow;

import idas.chox.core.services.DataService;

/**
 *
 * @author emmanuel
 */
public interface WorkflowContext {

    public DataService getDataService();

    public void setDataService(DataService dataService);

}
