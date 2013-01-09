package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.AuditTrailService;

public class SwitchClaim extends BaseActivity {
    
    private static final Logger LOG = LoggerFactory.getLogger(SwitchClaim.class);
    private AuditTrailService auditTrailService;
    
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH) && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)
                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_FNOL) && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CR)
                && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_COM) && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to switch claim.");
        }

        if (claim.getInvoice() != null) {
            throw new AccessDeniedException("Cannot switch claim as it has an invoice attached.");
        }
        if (claim.getInsurer().getRelatedInsurer() == null) {
            throw new AccessDeniedException("Cannot switch claim as no related insurer is defined.");
        }
        
        if (ClaimType.isSubscriber(claim.getClaimType())) {
            // Make sure the new Insurer accepts subscriber claims
            Insurer newInsurer = claim.getInsurer().getRelatedInsurer();
            if (!newInsurer.isAllowSubscriberClaims()) {
                LOG.error("The selected Insurer '{}' does not allow Subscriber claims.", newInsurer.getName());
                throw new Exception("The selected Insurer does not allow Subscriber claims.");
            }
        }
        else if (ClaimType.isFixedFee(claim.getClaimType())) {
            // Make sure the new Insurer accepts fixed fee claims
            Insurer newInsurer = claim.getInsurer().getRelatedInsurer();
            if (!newInsurer.isAllowFixedFeeClaims()) {
                LOG.error("The selected Insurer '{}' does not allow Fixed Fee claims.", newInsurer.getName());
                throw new Exception("The selected Insurer does not allow Fixed Fee claims.");
            }
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
        claim.setLiabilityStatus(LiabilityStatus.LIABILITY_NULL);
        if(claim.getNotifications()!=null) {
            claim.getNotifications().removeAll(claim.getNotifications());
        }
        claim.setLiabilityAgreedDate(null);
        claim.setPercentageLiabilityCho(BigDecimal.ZERO);
        claim.setPercentageLiabilityAccepted(BigDecimal.ZERO);
        claim.setCreatedDate(new Date());

        LOG.debug("Switching Claim : Claim status has been updated");

        ThirdParty thirdParty = claim.getThirdParty();
        thirdParty.setInsurer(newInsurer);
        thirdParty.setInsurerBrand(newInsurer.getName());
        LOG.debug("Switching Claim: ThirdParty has been updated");

        String newComment = "Claim switched from '" + oldInsurer.getName() + "' to '" + newInsurer.getName() +"'";
        Comment comment = Comment.New(0, newComment);
        claim.addComment(comment);
        setMessage(newComment);
        LOG.debug("Switching Claim: Comment has been updated");
        
        // revert all Audits entries
        auditTrailService.revertAllAuditEntriesByClaimId(claim.getId());
    }
    
    @Override
    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Switching claim AFTER PROCESS method called");
        super.afterProcess(claim);
        LOG.info("Switching Claim : Claim {} has been switched to {}", claim.getChoReference(), claim.getInsurer().getName());
    }
    
    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);
    }
    
    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }
}
