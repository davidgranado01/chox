package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.hpi.*;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.History;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.VehicleClassPriceService;
import idas.chox.service.xml.util.NodeHelper;

import java.util.Date;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsurerUpload extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerUpload.class);
    private BreBandService breBandService;
    private VehicleClassPriceService vehicleClassPriceService;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setVehicleClassPriceService(VehicleClassPriceService vehicleClassPriceService) {
        this.vehicleClassPriceService = vehicleClassPriceService;
    }

    @Override
    protected void beforeProcess(Claim claim) {
        LOG.debug("Insurer Upload activity: in beforeProcess");
        if (claim.getHireMonitoringDetail() != null && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {
            claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
        }
        //Normalize caim number
        String claimNumber = claim.getClaimNumber();
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claim.setClaimNumber(claimNumber.trim());
        }
        LOG.debug("Insurer Upload activity: finished beforeProcess");
        
        claim.setAutoRoutedClaim(true);
        
        NodeHelper nodeHelper = new NodeHelper();
        if (ClaimType.isInsurerUpload(claim.getClaimType())
                && claim.getInsurer().getInsurerManualRegexExpression() != null 
                && !claim.getInsurer().getInsurerManualRegexExpression().equals("")
                && claimNumber != null 
                && !claimNumber.equals("")
                && !claim.getInsurer().isInsurerManualAutoRoutingEnable()
                && nodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getInsurerManualRegexExpression(), claimNumber.toUpperCase())) {
            claim.setAutoRoutedClaim(false); 
        }
        
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (!claim.isTransient()) {
            throw new Exception("A process new claim attempt failed due to claim is already exist.");
        }
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf(WebUserRole.ROLE_UPLOAD)) {
            throw new AccessDeniedException("Not in correct role to create a claim.");
        }
        LOG.debug("Insurer Upload activity validated.");
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        LOG.debug("Processing Insurer Upload activity...");

        // Set Claim BRE band
        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        claim.setBreBand(choBand);

        // Add General Note (specified in BRE band)
        if (choBand.getClaimUploadNote() != null && !choBand.getClaimUploadNote().trim().isEmpty()) {
            Comment comment = Comment.New(0, claim.getBreBand().getClaimUploadNote());
            claim.addComment(comment);
        }

        // Perform HPI check on customer vehicle
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
            if (claim.getCustomer() == null) 
                LOG.warn("Error getting HPI info: no customer available.");
            else
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getCustomer().getVehicleRegistration(), ex.getMessage());
            claim.getCustomer().setHpiError(ex.getMessage());
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
            if (claim.getVehicleHire() == null) 
                LOG.warn("Error getting HPI info: no vehicle hire available.");
            else
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getVehicleHire().getVehicleRegistration(), ex.getMessage());
            claim.getVehicleHire().setHpiError(ex.getMessage());
        }

        LOG.debug("Processing invoice for claim '{}'", claim.getChoReference());
        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);
        LOG.debug("Rules engine response received for claim '{}'", claim.getChoReference());
        for (History history : History.New(response)) {
            LOG.debug("Adding BRE history to claim '{}': {} - " + history.getNarrative(), claim.getChoReference(), history.getRuleId());
            claim.addHistory(history);
        }

        claim.setStatusModifiedDate(new Date());
        
        if (ClaimType.isInsurerUpload(claim.getClaimType())) {
            claim = routeInsurerUploadToAwaitingInvoicePayment(claim);
        }
        
    }

    private Claim routeInsurerUploadToAwaitingInvoicePayment(Claim claim) {
        boolean isEnableManualInvoiceWorkgroupOwnership = claim.getInsurer().isEnableManualInvoiceOwnership() || claim.getInsurer().isEnableManualInvoiceWorkgroups();
        
        if (ClaimStatus.INVOICE_APPROVED_BY_BRE.equals(claim.getStatus()) && claim.isAutoRoutedClaim()) {
            //in case invoice ownership is enabled we set it to the MANUAL_INVOICE_UNASSIGNED status and 
            //when assiggned to owner or workgroup we set it to the MANUAL_INVOICE_APPROVED/REJECTED
            
            if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().getInvoiceWorkgroup() != null) {
                claim.setWorkgroupOriginal(claim.getWorkgroup());
                claim.setWorkgroup(claim.getInsurer().getInvoiceWorkgroup());
            }

            //re-assign claim
            if (claim.getInsurer().isClaimOwnershipEnable() && claim.getInsurer().getInvoiceOwner() != null) {
                claim.setClaimOwnerOriginal(claim.getClaimOwner());
                claim.setClaimOwner(claim.getInsurer().getInvoiceOwner());
            }
            
            super.setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(super.getCurrentStatus());
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
            getDataService().save(claim);
            logTransaction(claim, super.getCurrentStatus(), claim.getStatus(), 1);
            // move claim to next status
            super.setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(super.getCurrentStatus());
            claim.setStatus(ClaimStatus.MANUAL_INVOICE_APPROVED);
            
            if(isEnableManualInvoiceWorkgroupOwnership)
                claim.setManualInvoiceApproved(true);
            
        } else {
            if(isEnableManualInvoiceWorkgroupOwnership){
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_UNASSIGNED);
                claim.setManualInvoiceApproved(false);
            } else {
                claim.setStatus(ClaimStatus.MANUAL_INVOICE_REJECTED);
            }
        }
        return claim;
    }
    
    @Override
    protected String getCurrentStatus() {
        return "";
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(null);
    }
}
