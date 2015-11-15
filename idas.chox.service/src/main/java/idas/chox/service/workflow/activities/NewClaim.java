package idas.chox.service.workflow.activities;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.hpi.Hpi;
import idas.chox.core.hpi.HpiException;
import idas.chox.core.hpi.HpiResponse;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.services.BreBandService;

public class NewClaim extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(NewClaim.class);
    private BreBandService breBandService;

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }


    @Override
    public boolean needsOwnershipCheck() {
        return false;
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
    protected void doProcess(Claim claim) throws Exception {
        claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        claim.setStatusModifiedDate(new Date());
        // Add note containing CHO telephone number
        if (claim.getChorganisation().getPhone() != null && claim.getChorganisation().getPhone().length() > 0) {
            /*
             *  The below check has been added to eliminate duplicate 
             *  CHO contact number comment when switching claim.
             */
            boolean canAddChoContactNumberComment = true;
            if (claim.getComments() != null) {
                for (Comment comment : claim.getComments()) {
                    if (comment.getComment().startsWith("CHO contact number") 
                            && !comment.isReverted()) {
                        canAddChoContactNumberComment = false;
                    }
                }
            }
            if (canAddChoContactNumberComment) {
                Comment comment = Comment.newComment(0, "CHO contact number is " + claim.getChorganisation().getPhone());
                claim.addComment(comment);
            }
        }

        // Set Claim BRE band
        BreBand choBand = breBandService.getBreBand(claim.getChorganisation().getId(), claim.getInsurer().getId());
        claim.setBreBand(choBand);
        
        if (choBand != null) {
            // Add General Note (specified in BRE band)
            if (choBand.getClaimUploadNote() != null && !choBand.getClaimUploadNote().trim().isEmpty()) {
                Comment comment = Comment.newComment(0, claim.getBreBand().getClaimUploadNote());
                claim.addComment(comment);
            }
            // For subscriber and fixed-fee claims, we need to set the initial SLA remaining fields
            if (ClaimType.isSubscriber(claim.getClaimType())) {
                if (choBand.getSubscriberSlaDays() != 0) {
                    claim.setRemainingSlaDaysInt(choBand.getSubscriberSlaDays() - 1);
                    if (choBand.getSubscriberSlaDays() == 1) {
                        claim.setRemainingSlaDays(claim.getBreBand().getSubscriberTimeCutOff());
                    } else {
                        claim.setRemainingSlaDays(String.valueOf(claim.getRemainingSlaDaysInt()));
                    }
                } else {
                    claim.setRemainingSlaDays("-");
                }
            } else if (ClaimType.isFixedFee(claim.getClaimType())) {
                if (choBand.getFixedFeeSlaDays() != 0) {
                    claim.setRemainingSlaDaysInt(choBand.getFixedFeeSlaDays() - 1);
                    if (choBand.getFixedFeeSlaDays() == 1) {
                        claim.setRemainingSlaDays(claim.getBreBand().getFixedFeeTimeCutOff());
                    } else {
                        claim.setRemainingSlaDays(String.valueOf(claim.getRemainingSlaDaysInt()));
                    }
                } else {
                    claim.setRemainingSlaDays("-");
                }
            }
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

}
