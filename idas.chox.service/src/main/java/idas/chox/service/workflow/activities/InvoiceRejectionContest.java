package idas.chox.service.workflow.activities;

import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.History;
import idas.chox.core.security.SecurityInfoProvider;
import org.hibernate.util.StringHelper;

public class InvoiceRejectionContest extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRejectionContest.class);
    private String supportingLiabilityNotes;

    public String getSupportingLiabilityNotes() {
        return supportingLiabilityNotes;
    }

    public void setSupportingLiabilityNotes(String supportingLiabilityNotes) {
        this.supportingLiabilityNotes = supportingLiabilityNotes;
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        LOG.debug("Validating InvoiceRejectionContest activity.");
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")
                && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to contest invoice rejection.");
        }
        LOG.debug("InvoiceRejectionContest activity validated ok.");
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Processing InvoiceRejectionContest activity.");



        /* RESUBMIT INVOICE FEOM CHO SHOULD PERFORM BRE VALIDATION AGAIN */
        RulesEngineResponse response = null;
        try {
            response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        } catch (Exception ex) {
            LOG.error("Exception processing re-submitted invoice: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            throw ex;
        }
        LOG.debug("Response received - adding to history.");
        for (History history : History.New(response)) {
            claim.addHistory(history);
        }
        LOG.debug("Setting status (current status is '{}'", claim.getStatus());
        LOG.debug("Setting status (response status is '{}'", response.getStatus(claim.getInsurer().isEngineersEnable()));
        if ((response.getStatus(claim.getInsurer().isEngineersEnable())).equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
            throw new Exception("ERROR : Invoice data calculation incorrect");
        } else {

            if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
                claim.addComment(Comment.New(0, "Supporting Notes: " + supportingLiabilityNotes));
            }
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), null, claim.getInvoice().getReasonOfRejection());

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
    }
}