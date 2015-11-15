package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.services.AuditTrailService;

public class SubscriberClaimToGta extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriberClaimToGta.class);
    private AuditTrailService auditTrailService;
    
    @Override
    protected void doProcess(Claim claim) {
        String comment = "Claim switched from Fixed Fee to GTA.";
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            comment = "Claim switched from Subscriber to GTA.";
        }

        claim.setStatus(auditTrailService.getStateBeforeRejection(claim.getId()));
        claim.setRemainingSlaDays(null);
        claim.setRemainingSlaDaysInt(null);
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
        claim.addComment(Comment.newComment(0, comment));
    }

    public AuditTrailService getAuditTrailService() {
        return auditTrailService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }
}
