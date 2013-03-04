package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.History;
import idas.chox.service.xml.util.NodeHelper;

public class InvoiceResubmit extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceResubmit.class);
    private boolean autoRoutedInvoice = false;

    @Override
    protected void beforeProcess(Claim claim) {

        String claimNumber = claim.getThirdParty().getClaimReference();
        //TPI claim type is handled in NewTpiClaim activity
        //Insurer Upload claim type is handled in InsurerUpload activity
        if (ClaimType.isGTA(claim.getClaimType())
                && claim.getInsurer().isGtaAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getGtaRegexExpression() == null
                    || claim.getInsurer().getGtaRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getGtaRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        } else if (ClaimType.isSubscriber(claim.getClaimType())
                && claim.getInsurer().isSubscriberAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getSubscriberRegexExpression() == null
                    || claim.getInsurer().getSubscriberRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getSubscriberRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        } else if (ClaimType.isInsurerVsInsurer(claim.getClaimType())
                && claim.getInsurer().isInsurerVsInsurerAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getInsurerVsInsurerRegexExpression() == null
                    || claim.getInsurer().getInsurerVsInsurerRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getInsurerVsInsurerRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        } else if (ClaimType.isFixedFee(claim.getClaimType())
                && claim.getInsurer().isFixedFeeAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getFixedFeeRegexExpression() == null
                    || claim.getInsurer().getFixedFeeRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getFixedFeeRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
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

        if (autoRoutedInvoice && ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
            // re-route claim
            if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().getInvoiceWorkgroup() != null) {
                claim.setWorkgroupOriginal(claim.getWorkgroup());
                claim.setWorkgroup(claim.getInsurer().getInvoiceWorkgroup());
            }

            //re-assign claim
            if (claim.getInsurer().isClaimOwnershipEnable() && claim.getInsurer().getInvoiceOwner() != null) {
                claim.setClaimOwnerOriginal(claim.getClaimOwner());
                claim.setClaimOwner(claim.getInsurer().getInvoiceOwner());
            }
            getDataService().save(claim);
            logTransaction(claim, claim.getPreviousStatus(), claim.getStatus(), 0);
            // move claim to next status
            setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(getCurrentStatus());
            claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);

        }

    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        // If this is a TPI claim, we now need to process the chained NewTpiClaim activity
        if (getChainActivity() != null && ClaimType.isTPI(claim.getClaimType())) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        } else {
            LOG.debug("Saving Claim '{}' ", claim.getChoReference());
            getDataService().save(claim);
            logTransaction(claim);
        }
    }

}
