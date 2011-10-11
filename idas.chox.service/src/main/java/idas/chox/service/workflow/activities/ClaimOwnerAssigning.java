package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class ClaimOwnerAssigning extends BaseActivity {

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_COM")  &&!securityInfoProvider.isInRoleOf("ROLE_INS_MNG")
                && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to close a claim.");
        }

    }

    @Override
    public boolean isRequired(Claim claim) {
        //process only in claim status is CLAIM_UNACKNOWLEDGED_ROUTED
        return claim != null && (claim.getInsurer() != null
                && claim.getInsurer().isClaimOwnershipEnable());
    }

    @Override
    protected void beforeProcess(Claim claim) throws Exception {
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);

    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
    }
}
