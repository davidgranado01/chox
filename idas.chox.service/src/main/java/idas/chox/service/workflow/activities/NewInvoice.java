package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.History;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class NewInvoice extends BaseActivity {

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")) {
            throw new AccessDeniedException("Not in correct role to upload an invoice.");
        }
    }


    @Override
    protected void doProcess(Claim claim) throws Exception {
        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);

        for (History history : History.New(response)) {
            claim.addHistory(history);
        }

        claim.setStatus(response.getStatus());
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }
}
