package idas.chox.service.workflow.activities;

import java.util.Date;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.hpi.*;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUserRole;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.BreBandService;

public class NewClaim extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(NewClaim.class);
    private BreBandService breBandService;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }


    @Override
    protected void beforeProcess(Claim claim) {
        if (claim.getHireMonitoringDetail() != null && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {

            claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
        }
        //Normalize caim number
        String claimNumber = claim.getClaimNumber();
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claim.setClaimNumber(claimNumber.trim());
        }
    }

    @Override
    protected void validate(Claim claim) throws Exception {
        // The claim should not have id assigned to it unless it is being switched.
        if (!claim.isTransient() && !claim.isSwitchingClaim()) {
            throw new Exception("A process new claim attempt failed due to claim is already exist.");
        }
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        // Only CHO can create a claim except when claim being switched.
        if (!securityInfoProvider.isInRoleOf(WebUserRole.ROLE_CHO) && !claim.isSwitchingClaim()) {
            throw new AccessDeniedException("Not in correct role to create a claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claim.setStatusModifiedDate(new Date());
        // Add note containing CHO telephone number
        if (claim.getChorganisation().getPhone() != null && claim.getChorganisation().getPhone().length() > 0) {
            /*
             *  The below check has been added to eliminate duplicate 
             *  CHO contact number comment when switching claim.
             */
            boolean canAddChoContacNumberComment = true;
            if (claim.getComments() != null) {
                for (Comment comment : claim.getComments()) {
                    if (comment.getComment().startsWith("CHO contact number") 
                            && !comment.isReverted()) {
                        canAddChoContacNumberComment = false;
                    }
                }
            }
            if (canAddChoContacNumberComment) {
                Comment comment = Comment.New(0, "CHO contact number is " + claim.getChorganisation().getPhone());
                claim.addComment(comment);
            }
        }

        // Set Claim BRE band
        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        claim.setBreBand(choBand);
        
        // Add General Note (specified in BRE band)
        if (choBand.getClaimUploadNote() != null && !choBand.getClaimUploadNote().trim().isEmpty()) {
            Comment comment = Comment.New(0, claim.getBreBand().getClaimUploadNote());
            claim.addComment(comment);
        }

        // Perform HPI check
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
            LOG.warn("Error getting HPI info for vrn '{}': {}",  claim.getCustomer().getVehicleRegistration(), ex.getMessage());
            claim.getCustomer().setHpiError(ex.getMessage());
        }

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
