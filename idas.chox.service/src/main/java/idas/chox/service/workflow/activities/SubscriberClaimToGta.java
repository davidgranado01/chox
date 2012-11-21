package idas.chox.service.workflow.activities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AuditTrailService;

public class SubscriberClaimToGta extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriberClaimToGta.class);
    private AuditTrailService auditTrailService;
    
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to send subcriber claim to GTA.");
        }
        if (!ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType())) {
            LOG.error("Attempt to switch claim to GTA for non-subscriber/fixed-fee claim: {}", claim.getChoReference());
            throw new AccessDeniedException("Not a subscriber/fixed-fee claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        String comment = "Claim switched from Fixed Fee to GTA.";
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            comment = "Claim switched from Subscriber to GTA.";
        }

        claim.setStatus(auditTrailService.getStateBeforeRejection(claim.getId()));
        if (claim.getClaimType() == ClaimType.SUBSCRIBER || claim.getClaimType() == ClaimType.FIXED_FEE) {
            claim.setClaimType(ClaimType.GTA);
        } else if (claim.getClaimType() == ClaimType.SUBSCRIBER_ORIGINAL_INVOICE
                    || claim.getClaimType() == ClaimType.FIXED_FEE_ORIGINAL_INVOICE) {
            claim.setClaimType(ClaimType.GTA_ORIGINAL_INVOICE);
        } else if (claim.getClaimType() == ClaimType.SUBSCRIBER_SUPPLEMENTARY_INVOICE
                    || claim.getClaimType() == ClaimType.FIXED_FEE_SUPPLEMENTARY_INVOICE ) {
            claim.setClaimType(ClaimType.GTA_SUPPLEMENTARY_INVOICE);
        } else { // not possible
            LOG.error("Attempt to switch a non-subcriber claim to GTA: {}", claim.getChoReference());
            throw new IllegalStateException("Claim not a Subscriber or Fixed-Fee claim.");
        }
        claim.addComment(Comment.New(0, comment));
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
    }

    public AuditTrailService getAuditTrailService() {
        return auditTrailService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }
}
