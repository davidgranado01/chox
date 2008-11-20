/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions.ins;

import chox.model.ClaimStatus;
import chox.services.ClaimService;
import chox.web.actions.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class InboxAction extends BaseAction {

    private ClaimService service;
    private Hashtable counterTable = new Hashtable();
    private List<String> availableStatus = new ArrayList<String>();

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    public InboxAction() {

        //init status table
       availableStatus.add(ClaimStatus.CLAIM_ACKNOWLEDGED);
       availableStatus.add(ClaimStatus.CLAIM_UNACKNOWLEDGED);
    }
    
    public Long getUnacknowledgedClaimCount()
    {
        return (Long)counterTable.get(ClaimStatus.CLAIM_UNACKNOWLEDGED);
    }
    
    public Long getAcknowledgedClaimCount()
    {
        return (Long)counterTable.get(ClaimStatus.CLAIM_ACKNOWLEDGED);
    }

    @Override
    public String execute() throws Exception {

        counterTable.clear();
       
        for (String s : availableStatus) {
            Long count = (Long)service.getCountByStatus(s);
            counterTable.put(s, count);
        }

        return SUCCESS;
    }
}