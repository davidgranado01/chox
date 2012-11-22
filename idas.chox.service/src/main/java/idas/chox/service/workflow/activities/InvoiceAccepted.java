package idas.chox.service.workflow.activities;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.security.SecurityInfoProvider;

public class InvoiceAccepted extends BaseActivity {

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_SCR") && !securityInfoProvider.isInRoleOf("ROLE_INS_CH")
                    && !securityInfoProvider.isInRoleOf("ROLE_INS_MNG") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to accept invoice.");
        }
    }

    @Override
    protected void doProcess(Claim claim) {
        if ( claim.getLiabilityStatus() != LiabilityStatus.LIABILITY_NULL
                && !ClaimType.isInsurerVsInsurer(claim.getClaimType())
                && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType()) &&
            ( claim.getLiabilityStatus().equals(LiabilityStatus.LIABILITY_DISPUTED)
             || claim.getLiabilityStatus().equals(LiabilityStatus.LIABILITY_UNKNOWN)
             || claim.getLiabilityStatus().equals(LiabilityStatus.LIABILITY_REPUDIATED))) {
            claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);

        }else{
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_APPROVED_BY_BRE);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_ENG);
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        expectingStatuses.add(ClaimStatus.INVOICE_REF_TO_CH);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED);
        expectingStatuses.add(ClaimStatus.INVOICE_ESCALATED_TO_CH);
        expectingStatuses.add(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
    }
    
}
