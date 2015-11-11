package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;

public class ClaimRejectionContest extends BaseActivity {
    
    @Override
    protected void doProcess(Claim claim) {
        switch (claim.getPreviousStatus()) {
            case ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED:
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
                break;
            case ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED:
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
                break;
            default:
                claim.setStatus(ClaimStatus.CLAIM_REJECTION_CONTESTED);
                break;
        }
        
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), claim.getReasonOfRejection(), null);
        // Update Remaining SLA Days for Fixed-Fee and Subscriber claims
        if (ClaimType.isFixedFee(claim.getClaimType())) {
            int fixedFeeSlaDays = claim.getBreBand().getFixedFeeSlaDays();
            if (fixedFeeSlaDays != 0) {
                int claimDays = fixedFeeSlaDays + claim.getSlaExtDays() - claimService.getFixedFeeClaimDays(claim.getId());
                claim.setRemainingSlaDaysInt(claimDays);
                if (claimDays == 0) {
                    claim.setRemainingSlaDays(claim.getBreBand().getFixedFeeTimeCutOff());
                } else {
                    claim.setRemainingSlaDays(String.valueOf(claim.getRemainingSlaDaysInt()));
                }
            }
        } else if (ClaimType.isSubscriber(claim.getClaimType())) {
            int subscriberSlaDays = claim.getBreBand().getSubscriberSlaDays();
            if (subscriberSlaDays != 0) {
                int claimDays = subscriberSlaDays + claim.getSlaExtDays() - claimService.getSubscriberClaimDays(claim.getId());
                claim.setRemainingSlaDaysInt(claimDays);
                if (claimDays == 0) {
                    claim.setRemainingSlaDays(claim.getBreBand().getSubscriberTimeCutOff());
                } else {
                    claim.setRemainingSlaDays(String.valueOf(claim.getRemainingSlaDaysInt()));
                }
            }
        }
        activityEventGenerator.generate(claim, this);

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

}
