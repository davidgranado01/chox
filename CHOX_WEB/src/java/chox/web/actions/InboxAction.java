/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.ClaimStatus;
import chox.services.ClaimService;
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
    
    public Long getAwaitingPaymentPackCount()
    {
         return (Long)counterTable.get(ClaimStatus.AWAITING_PAYMENT_PACK);
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