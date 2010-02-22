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
import idas.chox.service.workflow.ActivityFactory;

public class BatchUpdateAction extends BaseAction {

    private ClaimService claimService;
    private UserService userService;
    private AuditTrailService auditTrailService;
    private String actionResult;
    private List<Integer> selectedClaimIdList;
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

    public String doClaimOwnershipAction() {


        String oldStatus = ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED;

        // WORKGROUP
        Workgroup workgroupDBA = new Workgroup();
        if (this.workgroupId != null && this.workgroupId > 0) {
            workgroupDBA = workgroupService.getWorkgroup(this.workgroupId);
        }

        // CLAIM OWNERSHIP
        WebUser claimOwnerDBA = new WebUser();
        claimOwnerDBA = userService.getWebUser(this.claimOwnerId);

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

        // WORKGROUP
        Workgroup workgroupDBA = new Workgroup();
        if (this.workgroupId != null && this.workgroupId > 0) {
            workgroupDBA = workgroupService.getWorkgroup(this.workgroupId);
        }

        // CLAIM OWNERSHIP
        WebUser claimOwnerDBA = userService.getWebUser(this.claimOwnerId);

        // UPDATE CLAIMS(s)
        for (Integer id : selectedClaimIdList) {

            Claim claim = claimService.getClaim(id);

            String oldClaimOwnerName = "-";
            if (claim.getClaimOwner() != null) {
                oldClaimOwnerName = claim.getClaimOwner().getFullName();
            }

            String noteMsg = "Claim owner changed from '" + oldClaimOwnerName + "' to '" + claimOwnerDBA.getFullName() + "'";

            if (this.workgroupId != null && this.workgroupId > 0) {
                claim.setWorkgroup(workgroupDBA);
            }

            claim.setClaimOwner(claimOwnerDBA);
            claimService.updateClaim(claim);

            // SAVE NEW NOTE
            int noteVisibilityType = 0;
            createNewNote(noteMsg, noteVisibilityType, "", claim);

        }

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

    @Override
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
