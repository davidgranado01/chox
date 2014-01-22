package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.hpi.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.service.xml.util.NodeHelper;

public class NewTpiClaim extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(NewTpiClaim.class);
    private boolean autoRoutedInvoice = false;
    protected boolean newClaim = false;
    protected boolean claimRouted = false;
    protected boolean claimOwnerAssigned = false;
    protected boolean invoiceAccepted = false;
    
    public boolean isAutoRoutedInvoice() {
        return autoRoutedInvoice;
    }

    @Override
    public boolean needsOwnershipCheck() {
        return false;
    }

    @Override
    protected void beforeProcess(Claim claim) {
        if (claim.getStatus() == null && claim.getHireMonitoringDetail() != null
                    && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {
                claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
        }

        String claimNumber = claim.getThirdParty().getClaimReference();

        if (ClaimType.isTPI(claim.getClaimType())
                    && claim.getInsurer().isTpiAutoRoutingEnable() 
                    && (claimNumber == null || claim.getInsurer().getTpiRegexExpression() == null
                        || claim.getInsurer().getTpiRegexExpression().isEmpty()
                        || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getTpiRegexExpression(), claimNumber.toUpperCase()))) {
                autoRoutedInvoice = true;
            }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        //First Set Liability status
        claim.setLiabilityStatus(LiabilityStatus.LIABILITY_ACCEPTED);
        claim.setLiabilityAgreedDate(new Date());
        claim.setPercentageLiabilityAccepted(new BigDecimal("100.00"));
        claim.setPercentageLiabilityCho(BigDecimal.ZERO);
        getWorkflowContext().getClaimService().updateLiabilityPayment(claim);

        LOG.debug("New TPI Claim activity with claim '{}': status='{}', TPI status='{}'",
                new Object[] {claim.getChoReference(), claim.getStatus(), claim.getTpiClaimStatus()});
        if (claim.getStatus() == null) {
            LOG.debug("No claim status for TPI claim - must be new TPI claim, so setting to 'AwaitingInvoiceData'");
            claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
            claim.setStatusModifiedDate(new Date());
            if (claim.getChorganisation().getPhone() != null && claim.getChorganisation().getPhone().length() > 0) {
                Comment comment = Comment.newComment(0, "CHO contact number is " + claim.getChorganisation().getPhone());
                claim.addComment(comment);
            }
            try {
                HpiResponse response = Hpi.getHpiInfo(claim.getCustomer().getVehicleRegistration());
                claim.getCustomer().setHpiVehicleManufacturer(response.getManufacturer());
                claim.getCustomer().setHpiVehicleModel(response.getModel());
                claim.getCustomer().setHpiVehicleYear(response.getYear());
                claim.getCustomer().setHpiVehicleCapacity(response.getCapacity());
                claim.getCustomer().setHpiVehicleDoorplan(response.getDoorPlan());
                claim.getCustomer().setHpiVehicleTransmission(response.getTransmission());
                claim.getCustomer().setHpiFirstRegistration(response.getFirstRegistration());
            } catch (HpiException ex) {
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getCustomer().getVehicleRegistration(), ex.getMessage());
                claim.getCustomer().setHpiError(ex.getMessage());
            }
            getDataService().save(claim);
            logTransaction(claim);
            newClaim = true;
        }

        if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {
            LOG.debug("TPI Claim status is InvoiceDataCalculationsIncorrect.");
            // move claim to next status
            super.setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(super.getCurrentStatus());
            claim.setStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);

        } else if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_APPROVED_BY_BRE)) {
            LOG.debug("TPI Claim status is InvoiceApprovedByBRE.");
            
            if (!autoRoutedInvoice) {
                LOG.debug("Invoice not auto-routed so moving to InvoiceUnassigned");
                // move claim to next status
                super.setCurrentStatus(claim.getStatus());
                claim.setPreviousStatus(super.getCurrentStatus());
                claim.setStatus(ClaimStatus.INVOICE_UNASSIGNED);
            } else {
                LOG.debug("Auto-routing invoice and moving to AwaitingInvoiceData");
                if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().getInvoiceWorkgroup() != null) {
                        claim.setWorkgroup(claim.getInsurer().getInvoiceWorkgroup());
                        claimRouted = true;
                }
                if (claim.getInsurer().isClaimOwnershipEnable() && claim.getInsurer().getInvoiceOwner() != null) {
                    claim.setClaimOwner(claim.getInsurer().getInvoiceOwner());
                    if (claim.getInsurer().getInvoiceOwner().getTelephone() != null
                            && claim.getInsurer().getInvoiceOwner().getTelephone().length() > 0) {
                        Comment comment = Comment.newComment(0, "Insurer Claims Handler is '"
                                            + claim.getInsurer().getInvoiceOwner().getFullName() + "' (contact number: "
                                                        + claim.getInsurer().getInvoiceOwner().getTelephone() + ").");
                        claim.addComment(comment);
                    }
                    claimOwnerAssigned = true;
                }
                // move claim to next status
                super.setCurrentStatus(claim.getStatus());
                claim.setPreviousStatus(super.getCurrentStatus());
                claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
                getDataService().save(claim);
                logTransaction(claim, super.getCurrentStatus(), claim.getStatus(), 1);

                // move claim to next status
                super.setCurrentStatus(claim.getStatus());
                claim.setPreviousStatus(super.getCurrentStatus());

                //if BRE approves the invoice and TPI is selected it will go into following status
                claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);  
                invoiceAccepted = true;
            }
        } else if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_ESCALATED)
                    || claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_ESCALATED_TO_CH)) {
            LOG.debug("TPI Claim status is InvoiceEscalated or InvoiceEscalatedTolaimsHandler - moving to InvoiceUnassigned");
            // move claim to next status
            super.setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(super.getCurrentStatus());
            claim.setStatus(ClaimStatus.INVOICE_UNASSIGNED);
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        eventGenerator.generate(claim, this);
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        } else {
            LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
            getDataService().save(claim);
            logTransaction(claim, super.getCurrentStatus(), claim.getStatus(), 1);            
        }
    }

    @Override
    protected String getCurrentStatus() { 
        return "";
    }

    public void setAutoRoutedInvoice(boolean autoRoutedInvoice) {
        this.autoRoutedInvoice = autoRoutedInvoice;
    }

}
