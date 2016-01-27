package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.History;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.service.xml.util.NodeHelper;

public class InvoiceResubmit extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(InvoiceResubmit.class);
    private boolean autoRoutedInvoice = false;
    protected boolean claimOwnerAssigned = false;
    protected boolean claimRouted = false;
    protected boolean invoiceAccepted = false;
    protected RulesEngineResponse breResponse = null;
    
    public boolean isAutoRoutedInvoice() {
        return autoRoutedInvoice;
    }

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
        } else if (ClaimType.isCollaborationProtocol(claim.getClaimType())
                && claim.getInsurer().isColaborationProtocolAutoRoutingEnable()
                && (claimNumber == null || claim.getInsurer().getCollaborationProtocolRegexExpression() == null
                    || claim.getInsurer().getCollaborationProtocolRegexExpression().isEmpty()
                    || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getCollaborationProtocolRegexExpression(), claimNumber.toUpperCase()))) {
            autoRoutedInvoice = true;
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {

        claim.setCaseWithClientsSolicitor(false);
        try {
            breResponse = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        } catch (Exception ex) {
            LOG.error("Exception thrown in rules engine: {}", ex.getMessage());
            throw ex;
        }

        for (History history : History.New(breResponse)) {
            claim.addHistory(history);
        }

        LOG.debug("Response history added");

        if ((breResponse.getStatus(claim.getInsurer().isEngineersEnable())).equalsIgnoreCase(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
            LOG.debug("Throwing Exception:  Invoice data calculation incorrect");
            throw new Exception("ERROR : Invoice data calculation incorrect");
        }
        
        if (autoRoutedInvoice && ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
            invoiceAccepted = true;
            // re-route claim
            if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().getInvoiceWorkgroup() != null) {
                claim.setWorkgroupOriginal(claim.getWorkgroup());
                claim.setWorkgroup(claim.getInsurer().getInvoiceWorkgroup());
                claimRouted = true;
            }

            //re-assign claim
            if (claim.getInsurer().isClaimOwnershipEnable() && claim.getInsurer().getInvoiceOwner() != null) {
                claim.setClaimOwnerOriginal(claim.getClaimOwner());
                claim.setClaimOwner(claim.getInsurer().getInvoiceOwner());
                claimOwnerAssigned = true;
            }
        }

        if (claim.getBreBand().isPaymentTeamActive()
                && ((ClaimType.isGTA(claim.getClaimType()) && claim.getInsurer().isGtaPaymentsTeamEnable())
                    || (ClaimType.isSubscriber(claim.getClaimType()) && claim.getInsurer().isSubscriberPaymentsTeamEnable())
                    || (ClaimType.isInsurerVsInsurer(claim.getClaimType()) && claim.getInsurer().isInsurerVsInsurerPaymentsTeamEnable())
                    || (ClaimType.isInsurerUpload(claim.getClaimType()) && claim.getInsurer().isInsurerManualPaymentsTeamEnable())
                    || (ClaimType.isFixedFee(claim.getClaimType()) && claim.getInsurer().isFixedFeePaymentsTeamEnable())
                    || (ClaimType.isCollaborationProtocol(claim.getClaimType()) && claim.getInsurer().isCollaborationPaymentsTeamEnable()))
                && (!claim.getInsurer().isWorkgroupEnable() || claim.getWorkgroup() == null || !claim.getWorkgroup().isStpExcluded())) {
            claim.getInvoice().setPaymentTeam(true);
            if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
                invoiceAccepted = true;
            }
        }

        if (invoiceAccepted) {
            getDataService().save(claim);
            logTransaction(claim, claim.getPreviousStatus(), claim.getStatus(), 0);
            // move claim to next status
            setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(getCurrentStatus());
            if (!ClaimType.isInsurerVsInsurer(claim.getClaimType())
                && !ClaimType.isSubscriber(claim.getClaimType()) && !ClaimType.isFixedFee(claim.getClaimType()) &&
                   (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL
                    || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_UNKNOWN
                    || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_DISPUTED
                    || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_REPUDIATED)) {
                claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
            } else {
                    claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            }
        }

    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        activityEventGenerator.generate(claim, this);
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
