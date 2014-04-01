package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.NotificationService;

public class SwitchClaim extends BaseActivity {
    
    private static final Logger LOG = LoggerFactory.getLogger(SwitchClaim.class);
    private AuditTrailService auditTrailService;
    private NotificationService notificationService;
    private InsurerChorganisationService insurerChorganisationService;
    
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        if (claim.getInvoice() != null) {
            throw new AccessDeniedException("Cannot switch claim as it has an invoice attached.");
        }
        if (claim.getInsurer().getRelatedInsurer() == null) {
            throw new AccessDeniedException("Cannot switch claim as no related insurer is defined.");
        }
        
        Insurer newInsurer = claim.getInsurer().getRelatedInsurer();
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            // Make sure the new Insurer accepts subscriber claims
            if (!newInsurer.isAllowSubscriberClaims()) {
                LOG.error("The selected Insurer '{}' does not allow Subscriber claims.", newInsurer.getName());
                throw new Exception("The selected Insurer does not allow Subscriber claims.");
            }
        } else if (ClaimType.isFixedFee(claim.getClaimType())) {
            // Make sure the new Insurer accepts fixed fee claims
            if (!newInsurer.isAllowFixedFeeClaims()) {
                LOG.error("The selected Insurer '{}' does not allow Fixed Fee claims.", newInsurer.getName());
                throw new Exception("The selected Insurer does not allow Fixed Fee claims.");
            }
        } else if (ClaimType.isTPI(claim.getClaimType())) {
            // Make sure the new Insurer accepts tpi claims
            if (!newInsurer.isThirdPartyInterventionActivated()) {
                LOG.error("The selected Insurer '{}' does not allow TPI claims.", newInsurer.getName());
                throw new Exception("The selected Insurer does not allow TPI claims.");
            }
        } else if (ClaimType.isCollaborationProtocol(claim.getClaimType())) {
            // Make sure the new Insurer accepts Collaboration Protocol claims
            if (!newInsurer.isAllowCollaborationProtocolClaims()) {
                LOG.error("The selected Insurer '{}' does not allow Collaboration Protocol claims.", newInsurer.getName());
                throw new Exception("The selected Insurer does not allow Collaboration Protocol claims.");
            }
        }
        
        if (insurerChorganisationService.getInsurerChorganisations(claim.getInsurer().getRelatedInsurer().getId(), claim.getChorganisation().getId()).size() <= 0) {
            LOG.error("The selected Insurer '{}' is not mapped to the CHO '{}'.", claim.getInsurer().getRelatedInsurer().getName(), claim.getChorganisation().getName());
            throw new Exception("The selected Insurer '" + claim.getInsurer().getRelatedInsurer().getName() + "' is not mapped with '" + claim.getChorganisation().getName() + "'.");
        }
    }

    
    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("Switching claim status for claim: {} (id={})", claim.getChoReference(), claim.getId());

        Insurer oldInsurer = claim.getInsurer();
        Insurer newInsurer = oldInsurer.getRelatedInsurer();
        LOG.debug("Switching claim with CHO reference '{}' to {}", claim.getChoReference(), newInsurer.getName());
        
        claim.setInsurer(newInsurer);
        claim.setClaimOwner(null);
        claim.setWorkgroup(null);
        claim.setPreviousStatus(null);
        claim.setLiability(LiabilityStatus.LIABILITY_NULL);
        claim.setLiabilityAgreedDate(null);
        claim.setLiabilityModifiedDate(null);
        claim.setLiabilityStatusModifiedDate(null);
        claim.setPercentageLiabilityCho(BigDecimal.ZERO);
        claim.setPercentageLiabilityAccepted(BigDecimal.ZERO);
        claim.setCreatedDate(new Date());

        if (ClaimType.isSubscriber(claim.getClaimType()) || ClaimType.isFixedFee(claim.getClaimType())) {
            claim.setSlaExtDays(0);
        }
        
        LOG.debug("Switching Claim : Claim details has been updated");

        notificationService.removeAllNotifications(claim.getId());
        LOG.debug("Switching Claim : Claim notifications have been removed");

        ThirdParty thirdParty = claim.getThirdParty();
        thirdParty.setInsurer(newInsurer);
        thirdParty.setInsurerBrand(newInsurer.getName());
        LOG.debug("Switching Claim: ThirdParty has been updated");

        String newComment = "Claim switched from '" + oldInsurer.getName() + "' to '" + newInsurer.getName() +"'";
        Comment comment = Comment.newComment(0, newComment);
        claim.addComment(comment);
        setMessage(newComment);
        LOG.debug("Switching Claim: Comment has been updated");
        
        // revert all Audits entries
        auditTrailService.revertAllAuditEntriesByClaimId(claim.getId());
    }
    
    @Override
    protected void afterProcess(Claim claim) throws Exception {
        /*
         * Rather than calling super.afterProcess(), we'll process the next activity (NewClaim) ourselves.
         * This prevents the claim being saved and the transaction logged
         */
        claim.setStatus(null);
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }
    
    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
