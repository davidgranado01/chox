package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.security.SecurityInfoProvider;

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
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if ((!securityInfoProvider.isInRoleOf("ROLE_CHO") && !securityInfoProvider.getIsCHOXAdmin())
                || (securityInfoProvider.isInRoleOf("ROLE_CHO") && (claim.getChorganisation().getId().compareTo(
                                securityInfoProvider.getCurrentUser().getChorganisation().getId())) != 0)) {
            throw new AccessDeniedException("Not in correct role to reject a full payment.");
        }

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
        LOG.debug("Claim updated...");
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.INVOICE_PAYMENT_LOGGED);
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

        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            ((RevertClaim)getChainActivity()).setAmountReceived(interimPaymentReceived);
            getChainActivity().processInBatch(claim);
            setMessage(getChainActivity().getMessage());
        }
    }

    
}