package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.History;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;

public class InvoiceResubmit extends BaseActivity {

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")
                && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to re-submit invoice.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);

        for (History history : History.New(response)) {
            claim.addHistory(history);
        }

        if ((response.getStatus(claim.getInsurer().isEngineersEnable())).equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
            throw new Exception("ERROR : Invoice data calculation incorrect");
        } else {
            if (!claim.isTpiClaim()) {
                claim.setStatus(response.getStatus(claim.getInsurer().isEngineersEnable()));
            }else{
                claim.setTpiClaimStatus(response.getStatus(claim.getInsurer().isEngineersEnable()));
            }
        }

    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        if (!claim.isTpiClaim()) {
            getDataService().save(claim);
            logTransaction(claim);
        } else {

            if (chainActivity != null) {
                chainActivity.setWorkflowContext(processContext);
                chainActivity.processInBatch(claim);
            }
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);
    }
}
