package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AuditTrailService;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class SubscriberClaimToGta extends BaseActivity {
    private AuditTrailService auditTrailService;
    
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to send subcriber claim to GTA.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(auditTrailService.getSubscriberStateBeforeRejection(claim.getId()));
        claim.setClaimType(ClaimType.GTA);
        claim.addComment(Comment.New(0, "Claim switched from Subscriber to GTA."));
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
    }

    public AuditTrailService getAuditTrailService() {
        return auditTrailService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }
}
