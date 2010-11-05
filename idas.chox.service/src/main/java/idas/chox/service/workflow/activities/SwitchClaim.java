package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class SwitchClaim extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(SwitchClaim.class);
    private ClaimService claimService;

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CH) && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_INS_MNG)
                    && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_FNOL) && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CR)
                    && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_COM) && !securityInfoProvider.getIsCHOXAdmin()
                    && !securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CHO)) {
            throw new AccessDeniedException("Not in correct role to switch claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        LOG.debug("Switching claim status for claim: {} (id={})", claim.getChoReference(), claim.getId());
        if (claimService.switchClaim(claim.getId())) {
            LOG.info("Claim Switched for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
        } else {
            LOG.warn("Failed to Switch claim  for claim with id={} (Supplier reference '{}')", claim.getId(), claim.getChoReference());
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Switching claim AFTER PROCESS method called");
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
