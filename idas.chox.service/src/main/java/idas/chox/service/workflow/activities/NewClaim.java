package idas.chox.service.workflow.activities;

import idas.chox.core.hpi.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.Date;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewClaim extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(NewClaim.class);

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
        if (!claim.isTransient()) {
            throw new Exception("A process new claim attempt failed due to claim is already exist.");
        }
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")) {
            throw new AccessDeniedException("Not in correct role to create a claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claim.setStatusModifiedDate(new Date());
        // Add note containing CHO telephone number
        if (claim.getChorganisation().getPhone() != null && claim.getChorganisation().getPhone().length() > 0) {
            Comment comment = Comment.New(0, "CHO contact number is " + claim.getChorganisation().getPhone());
            claim.addComment(comment);
//            comment.setClaim(claim);
//            claim.getComments().add(comment);
        }
        
        // Add General Note (specified in BRE band)
        if (claim.getBreBand().getClaimUploadNote() != null && !claim.getBreBand().getClaimUploadNote().trim().isEmpty()) {
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
