package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.Workgroup;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;

public class AssignInvoiceOwner extends BaseActivity {

    private int manualInvoiceWorkgroupId;
    private int claimOwnerId;
    private WebUser claimOwner;
    private Workgroup workgroup;
    private boolean workgroupsEnabled;
    private boolean ownershipEnabled;

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        workgroupsEnabled = claim.getInsurer().isEnableManualInvoiceWorkgroups();
        ownershipEnabled = claim.getInsurer().isEnableManualInvoiceOwnership();

        if (workgroupsEnabled && manualInvoiceWorkgroupId <= 0) {
            throw new Exception("Invalid workgroup id. workgroupId : " + manualInvoiceWorkgroupId);
        } else if (workgroupsEnabled) {
            workgroup = (Workgroup) getDataService().get(Workgroup.class, manualInvoiceWorkgroupId);
            if (workgroup == null) {
                throw new Exception("Invalid workgroup id. workgroup is null.");
            }
            // Check if workgroup belongs to the Insurer
            if (workgroup.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("Workgroup does not belong to Insurer");
            }
        }

        if (ownershipEnabled && claimOwnerId <= 0) {
            throw new Exception("Invalid user id. id : " + claimOwnerId);
        } else if (ownershipEnabled){
            claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
            if (claimOwner == null) {
                throw new Exception("Invalid user id. claimOwner is null");
            }
            // Check if user belongs to the Insurer
            if (claimOwner.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
            }
        }

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)
                && !securityInfoProvider.getIsCHOXAdmin() && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_UPLOAD)
                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MI)) {
            throw new AccessDeniedException("Not in correct role to assign owner.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        if (ownershipEnabled) {
            claim.setClaimOwner(claimOwner);
        }
        if (workgroupsEnabled) {
            claim.setWorkgroup(workgroup);
        }
        if(claim.isManualInvoiceApproved()){
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        } else {
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
        }
        if (ownershipEnabled && claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
            Comment comment = Comment.New(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ").");
            claim.addComment(comment);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
    }

    public int getClaimOwnerId() {
        return claimOwnerId;
    }

    public void setClaimOwnerId(int claimOwnerId) {
        this.claimOwnerId = claimOwnerId;
    }

    public void setClaimOwnerIdField(int claimOwnerIdField) {
        this.claimOwnerId = claimOwnerIdField;
    }

    public void setWorkgroupIdField(int workgroupIdField) {
        this.manualInvoiceWorkgroupId = workgroupIdField;
    }

    public int getManualInvoiceWorkgroupId() {
        return manualInvoiceWorkgroupId;
    }

    public void setManualInvoiceWorkgroupId(int manualInvoiceWorkgroupId) {
        this.manualInvoiceWorkgroupId = manualInvoiceWorkgroupId;
    }
}
