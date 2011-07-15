package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.History;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceResubmit extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceResubmit.class);

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
        RulesEngineResponse response = null;

        try {
            response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        } catch (Exception ex) {
            LOG.error("Exception thrown in rules engine: {}", ex.getMessage());
        }

        for (History history : History.New(response)) {
            claim.addHistory(history);
        }

        LOG.debug("Response history added");

        if ((response.getStatus(claim.getInsurer().isEngineersEnable())).equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
            LOG.debug("Throwing Exception:  Invoice data calculation incorrect");
            throw new Exception("ERROR : Invoice data calculation incorrect");
            /*
             * removed below code because it's already set in BusinessRulesEngServiceImpl class , in the processResubmitInvoice method.
             */
//        } else {
//            if (!claim.isTpiClaim()) {
//                claim.setStatus(response.getStatus(claim.getInsurer().isEngineersEnable()));
//            }else{
//                claim.setTpiClaimStatus(response.getStatus(claim.getInsurer().isEngineersEnable()));
//            }
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
