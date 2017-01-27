package idas.chox.service.workflow.activities;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

public class FullPaymentNotReceived extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(FullPaymentNotReceived.class);
    private BigDecimal interimPaymentReceived; 

    public BigDecimal getInterimPaymentReceived() {
        return interimPaymentReceived;
    }

    public void setInterimPaymentReceived(BigDecimal interimPaymentReceived) {
        this.interimPaymentReceived = interimPaymentReceived;
    }
    
    @Override
    protected void doProcess(Claim claim) {
        if (claim.getInvoice().getInterimPaymentMade() != null) {
            claim.getInvoice().setInterimPaymentMade(claim.getInvoice().getInterimPaymentMade().add(interimPaymentReceived));
        } else {
            claim.getInvoice().setInterimPaymentMade(interimPaymentReceived);
        }
        if (claim.getInvoice().getInterimPaymentReceived() != null) {
            claim.getInvoice().setInterimPaymentReceived(claim.getInvoice().getInterimPaymentReceived().add(interimPaymentReceived));
        } else {
            claim.getInvoice().setInterimPaymentReceived(interimPaymentReceived);
        }
        if (claim.getInsurer().isPaymentDisputesEnable()) {
            claim.setPaymentDispute(true);
        }
        LOG.debug("Claim updated...");
    }

    /*
     * We'll override the afterProcess as we need to feed in the interimPaymentReceived amount
     * to the RevertClaim activity (in order to generate the correct comment/note)
     */
    @Override
    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
        getDataService().save(claim);
        LOG.debug("Claim saved - logging transaction...");
        logTransaction(claim);
        LOG.debug("Claim saved & transaction logged.");
//        activityEventGenerator.generate(claim, this);
        activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getMBassador().post(event).now();
        });

        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            ((RevertClaim)getChainActivity()).setAmountReceived(interimPaymentReceived);
            getChainActivity().processInBatch(claim);
            setMessage(getChainActivity().getMessage());
        }
    }

    
}