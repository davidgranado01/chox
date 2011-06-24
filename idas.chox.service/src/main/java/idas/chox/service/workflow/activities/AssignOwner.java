package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.model.Workgroup;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class AssignOwner extends BaseActivity {

    private int oasWorkgroupId;
    private int claimOwnerId;
    private WebUser claimOwner;
    private Workgroup workgroup;
    private boolean workgroupsEnabled;

    @Override
    protected void validate(Claim claim) throws Exception {
        if (claim.isTpiClaim()) {
            expectingStatuses.clear();
            expectingStatuses.add(ClaimStatus.INVOICE_UNASSIGNED);
        }
        super.validate(claim);
        workgroupsEnabled = claim.getInsurer().isWorkgroupEnable();

        if (workgroupsEnabled && oasWorkgroupId <= 0) {
            throw new Exception("Invalid workgroup id. workgroupId : "+oasWorkgroupId);
        } else if (workgroupsEnabled) {
            workgroup = (Workgroup) getDataService().get(Workgroup.class, oasWorkgroupId);
            if (workgroup == null) {
                throw new Exception("Invalid workgroup id. workgroup is null.");
            }
        }

        if (claimOwnerId <= 0) {
            throw new Exception("Invalid user id. id : "+claimOwnerId);
        } else {
            claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
            if (claimOwner == null) {
                throw new Exception("Invalid user id. claimOwner is null");
            }
        }

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (   (!claim.isTpiClaim() && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)
                && !securityInfoProvider.getIsCHOXAdmin() && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_COM))
            || (claim.isTpiClaim() && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CR)
                 && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)
                 && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_COM ) && !securityInfoProvider.getIsCHOXAdmin())) {
            throw new AccessDeniedException("Not in correct role to assign owner.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        claim.setClaimOwner(claimOwner);
        if (workgroupsEnabled) {
            claim.setWorkgroup(workgroup);
        }
        if (!claim.isTpiClaim()) {
            claim.setIsFnolReviewed(false);
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        } else {
            claim.setStatus(claim.getTpiClaimStatus());
        }
        if (claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
            Comment comment = Comment.New(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ").");
            claim.addComment(comment);
        }
    }

  
    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
    }

    public int getOasWorkgroupId() {
        return oasWorkgroupId;
    }

    public void setOasWorkgroupId(int oasWorkgroupId) {
        this.oasWorkgroupId = oasWorkgroupId;
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
        this.oasWorkgroupId = workgroupIdField;
    }
}
