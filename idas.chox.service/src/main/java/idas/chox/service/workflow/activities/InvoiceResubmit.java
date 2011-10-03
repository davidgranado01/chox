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
        }
        
        // Check to see if we have an Insurer vs Insurer claim
        if (claim.isInsurerVsInsurerClaim()) {
            if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
                // re-route claim
                if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().getTpiWorkgroup() != null) {
                    if (claim.getWorkgroupOriginal() == null) {
                        claim.setWorkgroupOriginal(claim.getWorkgroup());
                    }
                    claim.setWorkgroup(claim.getInsurer().getTpiWorkgroup());
                }
                
                //re-assign claim
                if (claim.getInsurer().isClaimOwnershipEnable() && claim.getInsurer().getTpiClaimOwner() != null) {
                    if (claim.getClaimOwnerOriginal() == null) {
                        claim.setClaimOwnerOriginal(claim.getClaimOwner());
                    }
                    claim.setClaimOwner(claim.getInsurer().getTpiClaimOwner());
                }
            }
            getDataService().save(claim);
            logTransaction(claim, claim.getPreviousStatus(), claim.getStatus(), 0);
            // move claim to next status
            currentStatus = claim.getStatus();
            claim.setPreviousStatus(currentStatus);
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
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
