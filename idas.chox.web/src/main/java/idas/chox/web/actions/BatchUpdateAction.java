package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.model.Comment;

public class BatchUpdateAction extends BaseAction {

    private ClaimService claimService;
    private UserService userService;
    private String actionResult;
    private List<Integer> selectedClaimIdList;
    private Integer workgroupId; // CLAIM OWNERSHIP
    private Integer claimOwnerId;
    private WorkgroupService workgroupService;

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    public String doClaimOwnershipUpdateAction() {

        // WORKGROUP
        Workgroup workgroupDBA = null;
        if (this.workgroupId != null && this.workgroupId > 0) {
            workgroupDBA = workgroupService.getWorkgroup(this.workgroupId);
        }

        // CLAIM OWNERSHIP
        WebUser claimOwnerDBA = userService.getWebUser(this.claimOwnerId);

        // UPDATE CLAIMS(s)
        for (Integer id : selectedClaimIdList) {

            Claim claim = claimService.getClaim(id);
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }

            // Check workgroup belongs to the Insurer
            if (workgroupDBA != null &&  workgroupDBA.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("Workgroup does not belong to Insurer");
            }

            // Check user belongs to the Insurer
            if (claimOwnerDBA.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
            }

            String oldClaimOwnerName = "-";
            if (claim.getClaimOwner() != null) {
                oldClaimOwnerName = claim.getClaimOwner().getFullName();
            }

            String noteMsg = "Claim owner changed from '" + oldClaimOwnerName + "' to '" + claimOwnerDBA.getFullName() + "'";

            if (this.workgroupId != null && this.workgroupId > 0) {
                claim.setWorkgroup(workgroupDBA);
            }

            claim.setClaimOwner(claimOwnerDBA);
            
            //in case of manual invoice batch update we set the proper status
            if(claim.getStatus().equalsIgnoreCase(ClaimStatus.MANUAL_INVOICE_UNASSIGNED)){
                if(claim.isManualInvoiceApproved()){
                    claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
                }else{
                    claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
                }
            }
                
            claimService.updateClaim(claim);

            // SAVE NEW NOTE
            int noteVisibilityType = 0;
            createNewNote(noteMsg, noteVisibilityType, "", claim);
            if (claimOwnerDBA.getTelephone() != null && claimOwnerDBA.getTelephone().length() > 0) {
                String noteMsg2 = "Insurer Claims Handler is '" + claimOwnerDBA.getFullName() + "' (contact number: " + claimOwnerDBA.getTelephone() + ")";
                createNewNote(noteMsg2, noteVisibilityType, "", claim);
            }

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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    @Override
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
