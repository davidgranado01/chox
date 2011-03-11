package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
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
            throw new Exception("Invalid workgroup id.");
        } else if (workgroupsEnabled) {
            workgroup = (Workgroup) getDataService().get(Workgroup.class, oasWorkgroupId);
            if (workgroup == null) {
                throw new Exception("Invalid workgroup id.");
            }
        }

        if (claimOwnerId <= 0) {
            throw new Exception("Invalid user id.");
        } else {
            claimOwner = (WebUser) getDataService().get(WebUser.class, claimOwnerId);
            if (claimOwner == null) {
                throw new Exception("Invalid user id.");
            }
        }

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((!securityInfoProvider.isInRoleOf("ROLE_INS_MNG")
                && !securityInfoProvider.getIsCHOXAdmin() && !securityInfoProvider.isInRoleOf("ROLE_INS_COM")) || (!claim.isTpiClaim() && !securityInfoProvider.isInRoleOf("ROLE_INS_CR"))) {
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
    protected void afterProcess(Claim claim) throws Exception {
        getDataService().save(claim);
        logTransaction(claim);

        /*
         *  if the claim is TPI claim and special routed ( workgroup and owner assigned by chox ) and if the status invoice approved by bre , then
         *  move the claim directly to awaiting invoice payment status. 
         */
        if (claim.isTpiClaim() && claim.isSpecialRoutedTpiClaim() && claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_APPROVED_BY_BRE)) {
            currentStatus = claim.getStatus();
            claim.setPreviousStatus(currentStatus);
            claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
            getDataService().save(claim);
            logTransaction(claim, currentStatus, claim.getStatus(), 0);
            // move claim to next status
            currentStatus = claim.getStatus();
            claim.setPreviousStatus(currentStatus);
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            getDataService().save(claim);
            logTransaction(claim, currentStatus, claim.getStatus(), 0);
        }

        if (chainActivity != null) {
            chainActivity.setWorkflowContext(processContext);
            chainActivity.processInBatch(claim);
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
