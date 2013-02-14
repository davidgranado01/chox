package idas.chox.service.workflow.activities;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;

public class UpdateManualInvoicePaid extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateManualInvoicePaid.class);

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((!securityInfoProvider.isInRoleOf("ROLE_INS") && !securityInfoProvider.getIsCHOXAdmin())
                || (securityInfoProvider.isInRoleOf("ROLE_INS") && (claim.getInsurer().getId().compareTo(securityInfoProvider.getCurrentUser().getInsurer().getId())) != 0)) {
            throw new AccessDeniedException("Not in correct role to update Manual Invoice Payment.");
        }

    }

    @Override
    protected void doProcess(Claim claim) {

        claim.setStatus(ClaimStatus.MANUAL_INVOICE_PAID);
        LOG.debug("Claim {} have been moved to manual invoice paid status", claim.getChoReference());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_APPROVED);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_REJECTED);
        expectingStatuses.add(ClaimStatus.MANUAL_INVOICE_CONTESTED);
    }
}