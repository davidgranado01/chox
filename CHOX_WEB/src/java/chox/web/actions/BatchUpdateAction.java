/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.services.AuditTrailService;
import chox.services.ClaimService;
import chox.services.SystemLogService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emmanuel
 */
public class BatchUpdateAction extends BaseAction {

    private ClaimService claimService;
    private SystemLogService systemLogService;
    private AuditTrailService auditTrailService;
    private String actionResult;
    private List<Integer> selectedClaimIds;

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    // SPRINT 8
    public String doInvoicePaymentReceivedAction() {

        String oldStatus = ClaimStatus.INVOICE_PAYMENT_LOGGED;
        String newStatus = ClaimStatus.INVOICE_PAYMENT_RECEIVED;
        
        for (Integer id : selectedClaimIds) {
            Claim claim = claimService.getClaim(id);
            updateCliamStatus(claim,oldStatus,newStatus);
        }
        return SUCCESS;
    }

    public String doClaimRoutedAction() {

        String oldStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED;
        String newStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED;
        
        for (Integer id : selectedClaimIds) {
            Claim claim = claimService.getClaim(id);
            updateCliamStatus(claim, oldStatus, newStatus);
        }
        return SUCCESS;
    }
    
    public String clearBREApprovedInvoicesForPayment() {

        String oldStatus = ClaimStatus.INVOICE_APPROVED_BY_BRE;
        String newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        
        for (Integer id : selectedClaimIds) {
            Claim claim = claimService.getClaim(id);
            updateCliamStatus(claim,oldStatus,newStatus);
        }
        return SUCCESS;
    }
    
    public String logInvoicePayments() {

        String oldStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        String newStatus = ClaimStatus.INVOICE_PAYMENT_LOGGED;
       
        for (Integer id : selectedClaimIds) {
            Claim claim = claimService.getClaim(id);
            updateCliamStatus(claim,oldStatus,newStatus);
        }
        return SUCCESS;
    }
    
    private void updateCliamStatus(Claim claim,String oldStatus,String newStatus)
    {
        boolean bActionFlag = true;
        String sActionMsg = "";
        
        if (claim.getStatus().equalsIgnoreCase(oldStatus)) {
                try {

                    auditTrailService.logAuditLog(newStatus, claim, null, null);
                    sActionMsg = "ClaimId:" + claim.getId() + "| Status:" + newStatus;
                    claim.setStatus(newStatus);
                    claimService.updateClaim(claim);
                } catch (Exception ex) {
                    setActionResult("ERROR : " + ex.getMessage());
                    bActionFlag = false;
                    sActionMsg = getActionResult();
                } finally {
                    systemLogService.logSystemLog("ACT016", sActionMsg, bActionFlag, 3);
                }
            }
    }        

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public void setSelectedClaimIds(String selectedClaimIds) {
        String[] list = selectedClaimIds.split(",");
        
        this.selectedClaimIds = new ArrayList<Integer>();
        
        for(String s : list)
        {
            Integer selectedClaimId = Integer.parseInt(s);
            this.selectedClaimIds.add(selectedClaimId);
        }
        
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setSystemLogService(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public String getActionResult() {
        return actionResult;
    }
}
