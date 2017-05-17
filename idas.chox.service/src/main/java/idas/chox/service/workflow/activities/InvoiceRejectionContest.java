package idas.chox.service.workflow.activities;

import org.hibernate.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.History;
import idas.chox.events.BaseActivityEvent;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class InvoiceRejectionContest extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceRejectionContest.class);
    private String supportingLiabilityNotes;
    public RulesEngineResponse breResponse = null;

    public String getSupportingLiabilityNotes() {
        return supportingLiabilityNotes;
    }

    public void setSupportingLiabilityNotes(String supportingLiabilityNotes) {
        this.supportingLiabilityNotes = supportingLiabilityNotes;
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Processing InvoiceRejectionContest activity.");



        /* RESUBMIT INVOICE FEOM CHO SHOULD PERFORM BRE VALIDATION AGAIN */
        try {
            breResponse = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        } catch (Exception ex) {
            LOG.error("Exception processing re-submitted invoice: {}", ex.getMessage());
            if (ex.getCause() != null) {
                LOG.error("Caused by: {}", ex.getCause().getMessage());
            }
            throw ex;
        }
        LOG.debug("Response received - adding to history.");
        for (History history : History.New(breResponse)) {
            claim.addHistory(history);
        }
        LOG.debug("Setting status (current status is '{}'", claim.getStatus());
        LOG.debug("Setting status (response status is '{}'", breResponse.getStatus(claim.getInsurer().isEngineersEnable()));
        if ((breResponse.getStatus(claim.getInsurer().isEngineersEnable())).equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_CHO);
            throw new Exception("ERROR : Invoice data calculation incorrect");
        } else {

            if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
                claim.addComment(Comment.newComment(0, "Supporting Notes: " + supportingLiabilityNotes));
            }
            claim.setStatus(ClaimStatus.CONTESTED_INVOICE_REF_TO_INS);
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), null, claim.getInvoice().getReasonOfRejection());
        LOG.debug("Claim saved and transaction logged.");
//        activityEventGenerator.generate(claim, this);
        for (BaseActivityEvent event : activityEventGenerator.getEvents(claim, this)) {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        }
        if (getChainActivity() != null) {
            LOG.debug("Processing chained activity...");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
            LOG.debug("Finished Processing chained activity in InvoiceRejectionContest");
        } else {
            LOG.debug("Finished afterProcess.");
        }
    }

}