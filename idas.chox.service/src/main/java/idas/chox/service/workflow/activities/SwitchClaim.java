package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class SwitchClaim extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(SwitchClaim.class);

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
        claim.setPreviousStatus(claim.getStatus());
        claim.setStatusModifiedDate(new Date());
        claim.setLiabilityStatus(LiabilityStatus.LIABILITY_NULL);
        claim.getNotifications().removeAll(claim.getNotifications());
        claim.setLiabilityAgreedDate(null);
        claim.setPercentageLiabilityCho(BigDecimal.ZERO);
        claim.setPercentageLiabilityAccepted(BigDecimal.ZERO);
        claim.setCreatedDate(new Date());

        if (newInsurer.isWorkgroupEnable()) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        } else if (newInsurer.isClaimOwnershipEnable()) {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        } else {
            claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        }
        LOG.debug("Switching Claim : Claim status has been updated");

        ThirdParty thirdParty = claim.getThirdParty();
        thirdParty.setInsurer(newInsurer);
        thirdParty.setInsurerBrand(newInsurer.getName());
        LOG.debug("Switching Claim: ThirdParty has been updated");

        Comment comment = Comment.New(0, "Claim switched from " + oldInsurer.getName() + " to " + newInsurer.getName());
        claim.addComment(comment);
        LOG.debug("Switching Claim: Comment has been updated");
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
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REFERRED_TO_FNOL);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
        expectingStatuses.add(ClaimStatus.CLAIM_REF_TO_ENG);


    }
}
