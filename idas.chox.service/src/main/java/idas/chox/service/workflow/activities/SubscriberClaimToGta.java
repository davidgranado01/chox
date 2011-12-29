package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AuditTrailService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;

public class SubscriberClaimToGta extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriberClaimRejectionContest.class);
    private AuditTrailService auditTrailService;
    
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to send subcriber claim to GTA.");
        }
        if (!ClaimType.isSubscriber(claim.getClaimType())) {
            LOG.error("Attempt to switch subscriber claim to GTA for non-subscriber claim: {}", claim.getChoReference());
            throw new AccessDeniedException("Not a subscriber claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(auditTrailService.getSubscriberStateBeforeRejection(claim.getId()));
        if (claim.getClaimType() == ClaimType.SUBSCRIBER)
            claim.setClaimType(ClaimType.GTA);
        else if (claim.getClaimType() == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE)
            claim.setClaimType(ClaimType.GTA_ORIGINAL_INVOICE);
        else if (claim.getClaimType() == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE)
            claim.setClaimType(ClaimType.GTA_SUPPLEMENTARY_INVOICE);
        else { // not possible
            LOG.error("Attempt to switch a non-subcriber claim to GTA: {}", claim.getChoReference());
            throw new IllegalStateException("Claim not a subscriber claim.");
        }
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
