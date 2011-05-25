package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class ClaimReopen extends BaseActivity {

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        System.out.print(securityInfoProvider.getCurrentUser().getDisplayName() + "  "+securityInfoProvider.getIsCHOXAdmin());
        if (!securityInfoProvider.getIsCHO() && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to re-open claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(claim.getPreviousStatus());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_CLOSED);
    }
}