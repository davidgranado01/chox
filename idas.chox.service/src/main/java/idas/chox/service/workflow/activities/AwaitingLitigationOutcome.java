package idas.chox.service.workflow.activities;

import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;

public class AwaitingLitigationOutcome extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(AwaitingLitigationOutcome.class);

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        LOG.debug("Validating AwaitingLitigationOutcome activity.");
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")
                && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to Move Claim To Litigation Status.");
        }
        LOG.debug("AwaitingLitigationOutcome activity validated ok.");
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        
        LOG.debug("Processing AwaitingLitigationOutcome activity.");
        claim.setStatus(ClaimStatus.AWAITING_LITIGATION_OUTCOME);
           
    }
   
    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
    }
}