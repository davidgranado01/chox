/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.Comment;
import chox.model.WebUser;
import chox.model.Workgroup;
import chox.services.AuditTrailService;
import chox.services.ClaimService;
import chox.services.CommentService;
import chox.services.SystemLogService;
import chox.services.UserService;
import chox.services.WorkgroupService;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emmanuel
 */
public class BatchUpdateAction extends BaseAction {

    private ClaimService claimService;
    private UserService userService;
    private AuditTrailService auditTrailService;
    private String actionResult;
    private List<Integer> selectedClaimIdList;
    private int workgroup;
    private Integer workgroupId; // CLAIM OWNERSHIP
    private Integer claimOwnerId;
    private WorkgroupService workgroupService;
    private CommentService commentService;

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    public String doInvoicePaymentReceivedAction() {

        String oldStatus = ClaimStatus.INVOICE_PAYMENT_LOGGED;
        String newStatus = ClaimStatus.INVOICE_PAYMENT_RECEIVED;
        
        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);
            updateCliamStatus(claim, oldStatus, newStatus,0);
        }
        return SUCCESS;
    }

    public String doClaimRoutedAction() {

        String oldStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED;
        
        Workgroup workgroupDBA = new Workgroup();
        workgroupDBA = workgroupService.getObject(this.workgroupId);

        System.out.println("doClaimRoutedAction 01: "+workgroupDBA.getName());

        for (Integer id : selectedClaimIdList)  {

            Claim claim = claimService.getClaim(id);
            claim.setWorkgroup(workgroupDBA);
            updateCliamStatus(claim, oldStatus, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, 0);

            if(claim.getInsurer().isClaimOwnershipEnable()){
                updateCliamStatus(claim, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, 1);
            }

            System.out.println("doClaimRoutedAction 02: "+claim.getChoReference());
            
        }
        
        return SUCCESS;
    }

    public String doClaimOwnershipAction() {

        
        String oldStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED;

        // WORKGROUP
        Workgroup workgroupDBA = new Workgroup();
        if(this.workgroupId!=null && this.workgroupId>0){
            workgroupDBA = workgroupService.getObject(this.workgroupId);
        }

        // CLAIM OWNERSHIP
        WebUser claimOwnerDBA = new WebUser();
        claimOwnerDBA = userService.getObject(this.claimOwnerId);

        // UPDATE CLAIMS(s)
        for (Integer id : selectedClaimIdList)  {

            Claim claim = claimService.getClaim(id);

            if(this.workgroupId!=null && this.workgroupId>0){
                claim.setWorkgroup(workgroupDBA);
            }
            
            claim.setClaimOwner(claimOwnerDBA);
            updateCliamStatus(claim, oldStatus, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, 0);

            // createNewNote(claimOwnerDBA.getDisplayName(), true, claim);
            
        }

        return SUCCESS;
    }

/*
    private void createNewNote(String newClaimOwnerName, boolean isPublic, Claim claim) {

        String noteMsg = "Claim owner changed from 'N/A' to '" + newClaimOwnerName+"'";

        Comment comment = new Comment();
        comment.setIsPublic(isPublic);
        comment.setComment(noteMsg);
        comment.setClaim(claim);

        try {
            commentService.createNewObject(comment);
        } catch (Exception ex) {
            this.actionResult = "ERROR : " + ex.getMessage();
        }

    }
    */
    
    public String clearBREApprovedInvoicesForPayment() {

        String oldStatus = ClaimStatus.INVOICE_APPROVED_BY_BRE;
        String newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        
        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);
            updateCliamStatus(claim,oldStatus,newStatus, 0);
        }
        return SUCCESS;
    }
    
    public String logInvoicePayments() {

        String oldStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        String newStatus = ClaimStatus.INVOICE_PAYMENT_LOGGED;
       
        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);
            updateCliamStatus(claim,oldStatus,newStatus, 0);
        }
        return SUCCESS;
    }
    
    private void updateCliamStatus(Claim claim,String oldStatus,String newStatus, Integer secInterval)
    {
        if (claim.getStatus().equalsIgnoreCase(oldStatus)) {
            try {
                auditTrailService.logAuditLog(newStatus, claim, null, null, secInterval);
                claim.setStatus(newStatus);
                claimService.updateClaim(claim);
            } catch (Exception ex) {
                setActionResult("ERROR : " + ex.getMessage());
            }
        }
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }

    public void setSelectedClaimIds(String ids) {
        String[] list = ids.split(",");
        
        selectedClaimIdList = new ArrayList<Integer>();
        
        for(String s : list)
        {
            Integer id = Integer.parseInt(s.trim());
            selectedClaimIdList.add(id);
        }
        
    }

    public void setWorkgroup(int id){
        this.workgroup = id;
    }

    public Integer getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(Integer workgroupId) {
        this.workgroupId = workgroupId;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }
    
    public String getActionResult() {
        return actionResult;
    }

    public Integer getClaimOwnerId() {
        return claimOwnerId;
    }

    public void setClaimOwnerId(Integer claimOwnerId) {
        this.claimOwnerId = claimOwnerId;
    }

    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    
}
