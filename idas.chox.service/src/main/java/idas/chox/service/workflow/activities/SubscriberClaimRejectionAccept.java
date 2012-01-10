package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;

public class SubscriberClaimRejectionAccept extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriberClaimRejectionAccept.class);

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to contest subscriber claim rejection.");
        }
        if (!ClaimType.isSubscriber(claim.getClaimType())) {
            LOG.error("Attempt to accept subscriber claim rejection for non-subscriber claim: {}", claim.getChoReference());
            throw new AccessDeniedException("Not a subscriber claim.");
        }
    }


    @Override
    protected void doProcess(Claim claim) {
        // Subscriber claims move straight to AwaitingInvoiceData
        setCurrentStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        logTransaction(claim, ClaimStatus.SUBSCRIBER_CLAIM_REJECTED, claim.getReasonOfRejection(), null);
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }


    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
    }
}
