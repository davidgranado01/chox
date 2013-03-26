package idas.chox.service.workflow.activities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.hpi.*;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.History;
import idas.chox.core.services.BreBandService;
import idas.chox.service.xml.util.NodeHelper;

public class InsurerUpload extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerUpload.class);
    private BreBandService breBandService;
    private boolean autoRoutedInvoice = false;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    @Override
    protected void beforeProcess(Claim claim) {
        if (claim.getClaimType() == ClaimType.INSURER_INVOICE) {
            if (claim.getHireMonitoringDetail() != null && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {
                claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
            }
            //Normalize claim number
            String claimNumber = claim.getClaimNumber();
            if (claimNumber != null && !claimNumber.isEmpty()) {
                claim.setClaimNumber(claimNumber.trim());
            }
        }

        if (claim.getInsurer().isInsurerManualAutoRoutingEnable()
                && (claim.getClaimNumber() == null
                || claim.getInsurer().getInsurerManualRegexExpression() == null
                || claim.getInsurer().getInsurerManualRegexExpression().isEmpty()
                || !NodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getInsurerManualRegexExpression(), claim.getClaimNumber().toUpperCase()))) {
            autoRoutedInvoice = true;
        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Processing Insurer Upload activity with invoice '{}'...", claim.getInvoice());

        // Set Claim BRE band
        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        claim.setBreBand(choBand);

        // Add General Note (specified in BRE band)
        if (choBand.getClaimUploadNote() != null && !choBand.getClaimUploadNote().trim().isEmpty()) {
            Comment comment = Comment.newComment(0, claim.getBreBand().getClaimUploadNote());
            claim.addComment(comment);
        }

        // Perform HPI check on customer vehicle
        if (claim.getClaimType() == ClaimType.INSURER_INVOICE) {
            LOG.debug("Performing HPI check on customer vehicle...");
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
            } catch (Exception ex) {
                if (claim.getCustomer() == null) {
                    LOG.warn("Error getting HPI info: no customer available.");
                } else {
                    LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getCustomer().getVehicleRegistration(), ex.getMessage());
                }
                claim.getCustomer().setHpiError(ex.getMessage());
            }
        }
        // Perform HPI check on hire vehicle
        LOG.debug("Performing HPI check on hire vehicle...");
        try {
            HpiResponse hpiResponse = Hpi.getHpiInfo(claim.getVehicleHire().getVehicleRegistration());
            claim.getVehicleHire().setHpiVehicleManufacturer(hpiResponse.getManufacturer());
            claim.getVehicleHire().setHpiVehicleModel(hpiResponse.getModel());
            claim.getVehicleHire().setHpiVehicleYear(hpiResponse.getYear());
            claim.getVehicleHire().setHpiVehicleCapacity(hpiResponse.getCapacity());
            claim.getVehicleHire().setHpiVehicleDoorplan(hpiResponse.getDoorPlan());
            claim.getVehicleHire().setHpiVehicleTransmission(hpiResponse.getTransmission());
            claim.getVehicleHire().setHpiFirstRegistration(hpiResponse.getFirstRegistration());
        } catch (HpiException ex) {
            LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getVehicleHire().getVehicleRegistration(), ex.getMessage());
            claim.getVehicleHire().setHpiError(ex.getMessage());
        } catch (Exception ex) {
            if (claim.getVehicleHire() == null) {
                LOG.warn("Error getting HPI info: no vehicle hire available.");
            } else {
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getVehicleHire().getVehicleRegistration(), ex.getMessage());
                claim.getVehicleHire().setHpiError(ex.getMessage());
            }
        }

        LOG.debug("Processing invoice for claim '{}'", claim.getChoReference());
        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        LOG.debug("Rules engine response received for claim '{}'", claim.getChoReference());
        for (History history : History.New(response)) {
            LOG.debug("Adding BRE history to claim '{}': {} - " + history.getNarrative(), claim.getChoReference(), history.getRuleId());
            claim.addHistory(history);
        }

        claim.setStatusModifiedDate(new Date());

        boolean isEnableManualInvoiceWorkgroupOwnership = claim.getInsurer().isEnableManualInvoiceOwnership() || claim.getInsurer().isEnableManualInvoiceWorkgroups();

        if (autoRoutedInvoice && ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
            //in case invoice ownership is enabled we set it to the MANUAL_INVOICE_UNASSIGNED status and 
            //when assiggned to owner or workgroup we set it to the MANUAL_INVOICE_APPROVED/REJECTED

            if (claim.getInsurer().isEnableManualInvoiceWorkgroups() && claim.getInsurer().getInvoiceWorkgroup() != null) {
                claim.setWorkgroupOriginal(claim.getWorkgroup());
                claim.setWorkgroup(claim.getInsurer().getInvoiceWorkgroup());
            }

            //re-assign claim
            if (claim.getInsurer().isEnableManualInvoiceOwnership() && claim.getInsurer().getInvoiceOwner() != null) {
                claim.setClaimOwnerOriginal(claim.getClaimOwner());
                claim.setClaimOwner(claim.getInsurer().getInvoiceOwner());
            }

            if (isEnableManualInvoiceWorkgroupOwnership && claim.getClaimType() == ClaimType.INSURER_INVOICE) {
                super.setCurrentStatus(claim.getStatus());
                claim.setPreviousStatus(super.getCurrentStatus());
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
                getDataService().save(claim);
                logTransaction(claim, super.getCurrentStatus(), claim.getStatus(), 1);
            }

            // move claim to next status
            super.setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(super.getCurrentStatus());
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);

            if (isEnableManualInvoiceWorkgroupOwnership) {
                claim.setManualInvoiceApproved(true);
            }

        } else if (claim.getClaimType() == ClaimType.INSURER_INVOICE && ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus()) && !isEnableManualInvoiceWorkgroupOwnership) {
            claim.setManualInvoiceApproved(true);
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        } else if (claim.getClaimType() == ClaimType.INSURER_INVOICE && ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
            claim.setManualInvoiceApproved(true);
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
        } else if (claim.getClaimType() == ClaimType.INSURER_INVOICE) {
            claim.setManualInvoiceApproved(false);
            if (isEnableManualInvoiceWorkgroupOwnership) {
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
            } else {
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
            }
        } else {
            // Insurer Claim
            if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
                claim.setManualInvoiceApproved(true);
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
            } else {
                claim.setManualInvoiceApproved(false);
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
            }
        }
        LOG.debug("Finished InsurerUpload activity for claim '{}': invoice is {}", claim.getChoReference(), claim.getInvoice());

    }

    @Override
    protected String getCurrentStatus() {
        return "";
    }
}
