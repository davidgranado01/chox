package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WorkgroupService;
import java.util.ArrayList;
import java.util.List;
import idas.chox.core.model.Comment;

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

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    public String doInvoicePaymentReceivedAction() {

        String oldStatus = ClaimStatus.INVOICE_PAYMENT_LOGGED;
        String newStatus = ClaimStatus.INVOICE_PAYMENT_RECEIVED;

        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);
            updateClaimStatus(claim, oldStatus, newStatus, 0);
        }
        return SUCCESS;
    }

    public String doClaimRoutedAction() {

        String oldStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED;

        Workgroup workgroupDBA = new Workgroup();
        workgroupDBA = workgroupService.getObject(this.workgroupId);

        for (Integer id : selectedClaimIdList) {

            Claim claim = claimService.getClaim(id);
            claim.setWorkgroup(workgroupDBA);
            updateClaimStatus(claim, oldStatus, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, 0);

            if (claim.getInsurer().isClaimOwnershipEnable()) {
                updateClaimStatus(claim, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED, 1);
            }

        }

        return SUCCESS;
    }

    public String doClaimOwnershipAction() {


        String oldStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED;

        // WORKGROUP
        Workgroup workgroupDBA = new Workgroup();
        if (this.workgroupId != null && this.workgroupId > 0) {
            workgroupDBA = workgroupService.getObject(this.workgroupId);
        }

        // CLAIM OWNERSHIP
        WebUser claimOwnerDBA = new WebUser();
        claimOwnerDBA = userService.getObject(this.claimOwnerId);

        // UPDATE CLAIMS(s)
        for (Integer id : selectedClaimIdList) {

            Claim claim = claimService.getClaim(id);

            if (this.workgroupId != null && this.workgroupId > 0) {
                claim.setWorkgroup(workgroupDBA);
            }

            claim.setClaimOwner(claimOwnerDBA);
            updateClaimStatus(claim, oldStatus, ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED, 0);

        }

        return SUCCESS;
    }

    public String doClaimOwnershipUpdateAction() {


            System.out.println(">>> workgroupId : "+this.workgroupId);
            System.out.println(">>> claimOwnerId : "+this.claimOwnerId);

        // WORKGROUP
        Workgroup workgroupDBA = new Workgroup();
        if(this.workgroupId!=null && this.workgroupId>0){
            workgroupDBA = workgroupService.getObject(this.workgroupId);
        }

        // CLAIM OWNERSHIP
        WebUser claimOwnerDBA = userService.getObject(this.claimOwnerId);

        // UPDATE CLAIMS(s)
        for (Integer id : selectedClaimIdList)  {

            Claim claim = claimService.getClaim(id);

            System.out.println("01 claim : "+claim.getId());

            String noteMsg = "Claim owner changed from '" + claim.getClaimOwner().getDisplayName() + "' to '" + claimOwnerDBA.getDisplayName()+"'";

            if(this.workgroupId!=null && this.workgroupId>0){
                claim.setWorkgroup(workgroupDBA);
            }

            claim.setClaimOwner(claimOwnerDBA);
            claimService.updateClaim(claim);

            System.out.println("02 claim : "+claim.getId());

            // SAVE NEW NOTE
            int noteVisibilityType = 0;
            createNewNote(noteMsg, noteVisibilityType, "", claim);

        }

        System.out.println("**************");
        return SUCCESS;
    }

    private void createNewNote(String sComment, int noteVisibilityType, String strPrefix, Claim claim) {

        if (sComment.length() > 0) {
            Comment comment = new Comment();
            comment.setVisibilityType(noteVisibilityType);
            comment.setComment(strPrefix + sComment);

            claim.addComment(comment);

            try {
                claimService.updateClaim(claim);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
            }
        }
    }

    public String clearBREApprovedInvoicesForPayment() {

        String oldStatus = ClaimStatus.INVOICE_APPROVED_BY_BRE;
        String newStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;

        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);
            updateClaimStatus(claim, oldStatus, newStatus, 0);
        }
        return SUCCESS;
    }

    public String logInvoicePayments() {

        String oldStatus = ClaimStatus.AWAITING_INVOICE_PAYMENT;
        String newStatus = ClaimStatus.INVOICE_PAYMENT_LOGGED;

        for (Integer id : selectedClaimIdList) {
            Claim claim = claimService.getClaim(id);
            updateClaimStatus(claim, oldStatus, newStatus, 0);
        }
        return SUCCESS;
    }

    private void updateClaimStatus(Claim claim, String oldStatus, String newStatus, Integer secInterval) {
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

        for (String s : list) {
            Integer id = Integer.parseInt(s.trim());
            selectedClaimIdList.add(id);
        }

    }

    public void setWorkgroup(int id) {
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
   
}
