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
import idas.chox.core.model.KeoghsRequest;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.InsurerDiscountService;
import idas.chox.core.services.UserService;
import idas.chox.keoghs.Keoghs;
import idas.chox.service.xml.util.NodeHelper;

public class InsurerUpload extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerUpload.class);
    private BreBandService breBandService;
    private InsurerDiscountService insurerDiscountService;
    private UserService userService;
    private boolean autoRoutedInvoice = false;
    private Keoghs keoghs;
    protected boolean claimRouted = false;
    protected boolean claimOwnerAssigned = false;
    protected RulesEngineResponse breResponse;

    public void setKeoghs(Keoghs keoghs) {
        this.keoghs = keoghs;
    }

    public void setInsurerDiscountService(InsurerDiscountService insurerDiscountService) {
        this.insurerDiscountService = insurerDiscountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public RulesEngineResponse getBreResponse() {
        return breResponse;
    }
    
    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public boolean isAutoRoutedInvoice() {
        return autoRoutedInvoice;
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
        BreBand choBand = claim.getBreBand();
        if (choBand == null) {
            choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
            claim.setBreBand(choBand);
        }

        insurerDiscountService.applyGtaDiscount(claim);
        insurerDiscountService.applyInsurerDiscounts(claim, userService.findByUserName("system"), true);
        claimService.updateLiabilityPayment(claim);
        // Set initial penalty band
        claimService.setInitialPenaltyBand(claim);
        
        // Add General Note (specified in BRE band)
        if (choBand != null && choBand.getClaimUploadNote() != null && !choBand.getClaimUploadNote().trim().isEmpty()) {
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
        breResponse = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        LOG.debug("Rules engine response received for claim '{}'", claim.getChoReference());
        History.New(breResponse).stream().map((history) -> {
            LOG.debug("Adding BRE history to claim '{}': {} - " + history.getNarrative(), claim.getChoReference(), history.getRuleId());
            return history;
        }).forEach((history) -> {
            claim.addHistory(history);
        });

        claim.setStatusModifiedDate(new Date());

        boolean isEnableManualInvoiceWorkgroupOwnership = claim.getInsurer().isEnableManualInvoiceOwnership() || claim.getInsurer().isEnableManualInvoiceWorkgroups();
        boolean invoicePassedBre = false;
        boolean paymentsTeamInvoice = false;

        if (claim.getBreBand().isPaymentTeamActive()
                    && claim.getInsurer().isInsurerManualPaymentsTeamEnable()
                    && (!claim.getInsurer().isWorkgroupEnable() || claim.getWorkgroup() == null || !claim.getWorkgroup().isStpExcluded())) {
                paymentsTeamInvoice = true;
        }

        if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus())) {
            invoicePassedBre = true;
        }

        if (invoicePassedBre && autoRoutedInvoice) {
            // Claim status will have been advanced after being resubmitted - we need toreverse this
            super.setCurrentStatus(claim.getPreviousStatus() == null ? "" : claim.getPreviousStatus());
            claim.setPreviousStatus(super.getCurrentStatus());
            
            //in case invoice ownership is enabled we set it to the MANUAL_INVOICE_UNASSIGNED status and 
            //when assiggned to owner or workgroup we set it to the MANUAL_INVOICE_APPROVED/REJECTED

            if (autoRoutedInvoice && claim.getInsurer().isEnableManualInvoiceWorkgroups() && claim.getInsurer().getInvoiceWorkgroup() != null) {
                claim.setWorkgroupOriginal(claim.getWorkgroup());
                claim.setWorkgroup(claim.getInsurer().getInvoiceWorkgroup());
                claimRouted = true;
            }

            //re-assign claim
            if (autoRoutedInvoice && claim.getInsurer().isEnableManualInvoiceOwnership() && claim.getInsurer().getInvoiceOwner() != null) {
                claim.setClaimOwnerOriginal(claim.getClaimOwner());
                claim.setClaimOwner(claim.getInsurer().getInvoiceOwner());
                claimOwnerAssigned = true;
            }

            if (autoRoutedInvoice && isEnableManualInvoiceWorkgroupOwnership && claim.getClaimType() == ClaimType.INSURER_INVOICE) {
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
                getDataService().save(claim);
                logTransaction(claim, super.getCurrentStatus(), claim.getStatus(), -100);
                super.setCurrentStatus(claim.getStatus());
                claim.setPreviousStatus(super.getCurrentStatus());
            }

            // move claim to next status
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);

            if (isEnableManualInvoiceWorkgroupOwnership) {
                claim.setManualInvoiceApproved(true);
            }

        } else if (claim.getClaimType() == ClaimType.INSURER_INVOICE && invoicePassedBre && !isEnableManualInvoiceWorkgroupOwnership) {
            claim.setManualInvoiceApproved(true);
            super.setCurrentStatus("");
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
        } else if (claim.getClaimType() == ClaimType.INSURER_INVOICE && invoicePassedBre) {
            claim.setManualInvoiceApproved(true);
            super.setCurrentStatus("");
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
        } else if (claim.getClaimType() == ClaimType.INSURER_INVOICE) {
            claim.setManualInvoiceApproved(false);
            super.setCurrentStatus("");
            if (isEnableManualInvoiceWorkgroupOwnership) {
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
            } else {
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
            }
        } else {
            // Insurer Claim
            if (invoicePassedBre) {
                claim.setManualInvoiceApproved(true);
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
            } else {
                claim.setManualInvoiceApproved(false);
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
            }
        }
        
        if (paymentsTeamInvoice) {
            claim.getInvoice().setPaymentTeam(true);
        }
        
        if (invoicePassedBre && (paymentsTeamInvoice || autoRoutedInvoice) && ClaimStatus.MANUAL_INVOICE_APPROVED.equals(claim.getStatus())) {
            getDataService().save(claim);
            logTransaction(claim, claim.getPreviousStatus()==null? "": claim.getPreviousStatus(), claim.getStatus(), -50);
            // move claim to next status
            setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(getCurrentStatus());
            if (claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_NULL
                || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_UNKNOWN
                || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_DISPUTED
                || claim.getLiabilityStatus() == LiabilityStatus.LIABILITY_REPUDIATED    ) {
                claim.setStatus(ClaimStatus.AWAITING_LIABILITY_RESOLUTION);
            } else {
                    claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            }
        }

        // If not a supplementary claim, Queue to send to Keoghs for ADA fraud check
        if (choBand != null && choBand.isFraudCheckEnable() && !ClaimType.isSupplementaryInvoice(claim.getClaimType())) {
            try {
                KeoghsRequest request  = keoghs.queue(claim, "Invoice Upload");
                LOG.debug("New Insurer Invoice '{}' queued to Keoghs", claim.getChoReference());
            } catch (Exception ex) {
                LOG.error("Error sending new Insurer Invoice with choref '{}' to keoghs: {}", claim.getChoReference(), ex.getMessage(), ex);
            }
        }

        LOG.debug("Finished InsurerUpload activity for claim '{}': invoice is {}", claim.getChoReference(), claim.getInvoice());

    }

}
