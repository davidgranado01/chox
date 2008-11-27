/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.services.ClaimService;
import chox.web.data.FilterRecordCounter;

/**
 *
 * @author Emmanuel
 */
public class InboxAction extends BaseAction {

    private ClaimService service;
    private FilterRecordCounter filterRecordCounter;

    public void setClaimService(ClaimService service) {
        this.service = service;
    }  

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    public FilterRecordCounter getFilterRecordCounter() {
        
        if(filterRecordCounter == null)
        {
            filterRecordCounter = new FilterRecordCounter(service);
        }
        
        return filterRecordCounter;
    }
}