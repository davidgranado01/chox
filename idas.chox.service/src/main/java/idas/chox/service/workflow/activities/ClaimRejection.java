package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.util.StringHelper;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.services.ClaimService;
import idas.chox.core.util.DateHelper;

public class ClaimRejection extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimRejection.class);

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private String claimNumber;
    private String rejectionDescription;
    private BigDecimal indemnityAmount;
    private BigDecimal percentageLiabilityAccepted;
    private boolean isQuantumDispute;
    private boolean isInvoiceReviewRequired;
    private String engineerClaimReviewNotes;
    private String supportingLiabilityNotes;
    private int reasonOfRejectionId;
    private BigDecimal percentageLiabilityCho;
    private Date liabilityAgreedDate;
    private LiabilityStatus liabilityStatus;
    private ClaimService claimService;
    private ReasonOfRejection reasonOfRejection;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Parameters">
    public void setIndemnityAmount(BigDecimal indemnityAmount) {
        this.indemnityAmount = indemnityAmount;
    }

    public void setClaimNumber(String claimNumber) {
        if (claimNumber != null && !claimNumber.isEmpty()) {
            claimNumber = claimNumber.trim();
        }
        this.claimNumber = claimNumber;
    }

    public void setPercentageLiabilityAccepted(BigDecimal percentageLiabilityAccepted) {
        this.percentageLiabilityAccepted = percentageLiabilityAccepted;
    }

    public void setIsQuantumDispute(boolean isQuantumDispute) {
        this.isQuantumDispute = isQuantumDispute;
    }

    public void setIsInvoiceReviewRequired(boolean isInvoiceReviewRequired) {
        this.isInvoiceReviewRequired = isInvoiceReviewRequired;
    }

    public void setEngineerClaimReviewNotes(String engineerClaimReviewNotes) {
        this.engineerClaimReviewNotes = engineerClaimReviewNotes;
    }

    public void setReasonOfRejectionId(int reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }
    /**
     * @param percentageLiabilityCho the percentageLiabilityCho to set
     */
    public void setPercentageLiabilityCho(BigDecimal percentageLiabilityCho) {
        this.percentageLiabilityCho = percentageLiabilityCho;
    }


    /**
     * @param liabilityAgreedDate the liabilityAgreedDate to set
     */
    public void setLiabilityAgreedDate(Date liabilityAgreedDate) {
        this.liabilityAgreedDate = liabilityAgreedDate;
    }



    /**
     * @param liabilityStatus the liabilityStatus to set
     */
    public void setLiabilityStatus(LiabilityStatus liabilityStatus) {
        this.liabilityStatus = liabilityStatus;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    // </editor-fold>

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (liabilityStatus != null && liabilityStatus.equals(LiabilityStatus.LIABILITY_ACCEPTED)
                && (percentageLiabilityAccepted.compareTo(new BigDecimal(100.0)) != 0
                || percentageLiabilityCho.compareTo(BigDecimal.ZERO) != 0)) {
            LOG.error("Full Liability accepted but % not correct: ins={}, cho={}", percentageLiabilityAccepted, percentageLiabilityCho);
            throw new AccessDeniedException("Liability % not correct");
        }
        else if (liabilityStatus != null && liabilityStatus.equals(LiabilityStatus.LIABILITY_SPLIT)
                && (percentageLiabilityCho.add(percentageLiabilityAccepted).compareTo(new BigDecimal(100.0)) > 0
                    || percentageLiabilityCho.add(percentageLiabilityAccepted).compareTo(BigDecimal.ZERO) <= 0)) {
            LOG.error("Liability total must be > 0 and <= 100%: ins={}, cho={}", percentageLiabilityAccepted, percentageLiabilityCho);
            throw new AccessDeniedException("Total liability is > 100% or <= 0%");
        }
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_INS_CH") && !securityInfoProvider.isInRoleOf("ROLE_INS_MNG")
                    && !securityInfoProvider.isInRoleOf("ROLE_INS_CR")
                    && !securityInfoProvider.isInRoleOf("ROLE_INS_COM") && !securityInfoProvider.getIsCHOXAdmin()) {
            throw new AccessDeniedException("Not in correct role to reject claim.");
        }
        
        reasonOfRejection = getReasonOfRejection();
        if (reasonOfRejection == null) {
            throw new Exception("No Reason of Rejection provided");
        }

        if (ClaimType.isSubscriber(claim.getClaimType())) {
            // Verify Rejected with the 5 day SLA with 5 minute leeway
            int subscriberClaimDays = claimService.getSubscriberClaimDays(claim.getId());
            if (subscriberClaimDays > (DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays()) || (subscriberClaimDays == (DateHelper.SUBSCRIBER_SLA_DAYS + claim.getSlaExtDays()) && !DateHelper.isBefore3pm(5))) {
                if (claim.getSlaExtDays() > 0) {
                    throw new Exception("Cannot reject subscriber claim as the 5 day SLA + "+claim.getSlaExtDays()+" day extension limit has now been reached.");
                } else {
                    throw new Exception("Cannot reject subscriber claim as the 5 day SLA limit has now been reached.");
                }
            }
            // Validate Liability Status
            // TODO
        }
        else if (ClaimType.isFixedFee(claim.getClaimType())) {
            // Verify Rejected with the 14 day SLA with 5 minute leeway
            int fixedFeeClaimDays = claimService.getFixedFeeClaimDays(claim.getId());
            if (fixedFeeClaimDays > (DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays()) || (fixedFeeClaimDays == (DateHelper.FIXED_FEE_SLA_DAYS + claim.getSlaExtDays()) && !DateHelper.isBefore3pm(5))) {
                if (claim.getSlaExtDays() > 0) {
                    throw new Exception("Cannot reject fixed fee claim as the 14 day SLA + "+claim.getSlaExtDays()+" day extension limit has now been reached.");
                } else {
                    throw new Exception("Cannot reject fixed fee claim as the 14 day SLA limit has now been reached.");
                }
            }
            // Validate Liability Status
            // TODO
        }

    }

    @Override
    protected void beforeProcess(Claim claim) {
        LOG.debug("beforeProcess start claim version = {}", claim.getVersion());
        if (liabilityStatus != null && (claim.getLiabilityStatus()==LiabilityStatus.LIABILITY_NULL
                || !claim.getLiabilityStatus().equals(liabilityStatus)) ){

                String note;
                if ( claim.getLiabilityStatus()==LiabilityStatus.LIABILITY_NULL ){
                    note = "Liability status changed to '" + liabilityStatus+"'";
                }else{
                    note = "Liability status changed from '" + claim.getLiabilityStatus() + "' to '" + liabilityStatus+"'";
                }
                claim.setLiabilityStatus(liabilityStatus);
                Comment comment = Comment.New(0, note);
                comment.setClaim(claim);
                claim.addComment(comment);                
        }
        if (indemnityAmount != null) {
            claim.setIndemnityAmount(indemnityAmount);
        }
        if (percentageLiabilityAccepted != null) {
            claim.setPercentageLiabilityAccepted(percentageLiabilityAccepted);
        }
        claim.setIsInvoiceReviewRequired(isInvoiceReviewRequired);
        claim.setIsQuantumDispute(isQuantumDispute);
        claim.setReasonOfRejection(reasonOfRejection);
        claim.setIsFnolReviewed(false);
        if (percentageLiabilityCho != null) {
            claim.setPercentageLiabilityCho(percentageLiabilityCho);
        }
        if (liabilityAgreedDate != null) {
            claim.setLiabilityAgreedDate(liabilityAgreedDate);
        }

        if (claimNumber != null) {
            claim.setClaimNumber(claimNumber);
        }
        LOG.debug("beforeProcess end claim version = {}", claim.getVersion());

    }

    @Override
    protected void doProcess(Claim claim) {
    	
        if (StringHelper.isNotEmpty(engineerClaimReviewNotes)) {
            claim.addComment(Comment.New(0, engineerClaimReviewNotes));
        }

        if (StringHelper.isNotEmpty(supportingLiabilityNotes)) {
            claim.addComment(Comment.New(0, "Supporting Liability Notes: " + supportingLiabilityNotes));
        }
        
        if (reasonOfRejection != null) {
            claim.addComment(Comment.New(0, "Reason For Rejection: " + reasonOfRejection.getRorName()));
            if(rejectionDescription != null && !rejectionDescription.equals("")) {
                claim.addComment(Comment.New(0, "Supporting Rejection Notes: " + rejectionDescription));
            }
        }
        else {
            LOG.error("No 'Reason of Rejection' specified for claim '{}': {}", claim.getChoReference(), reasonOfRejectionId);
        }

        if (ClaimType.isSubscriber(claim.getClaimType())) {
            claim.setStatus(ClaimStatus.SUBSCRIBER_CLAIM_REJECTED);
        }
        else {
            claim.setStatus(ClaimStatus.CLAIM_REJECTED);
        }
    }

    protected ReasonOfRejection getReasonOfRejection() {
        ReasonOfRejection ror = null;
        if (reasonOfRejectionId > 0) {
            ror = (ReasonOfRejection) this.getDataService().get(ReasonOfRejection.class, reasonOfRejectionId);
        }
        return ror;
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {

        getDataService().save(claim);
        logTransaction(claim, getCurrentStatus(), claim.getReasonOfRejection(), null);

        if (getChainActivity() != null) {
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        }
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
        expectingStatuses.add(ClaimStatus.CLAIM_REJECTION_CONTESTED);
        expectingStatuses.add(ClaimStatus.CLAIM_PENDING);
        expectingStatuses.add(ClaimStatus.CLAIM_UPDATE_BY_ENG);
    }

    /**
     * @return the supportingLiabilityNotes
     */
    public String getSupportingLiabilityNotes() {
        return supportingLiabilityNotes;
    }

    /**
     * @param supportingLiabilityNotes the supportingLiabilityNotes to set
     */
    public void setSupportingLiabilityNotes(String supportingLiabilityNotes) {
        this.supportingLiabilityNotes = supportingLiabilityNotes;
    }

	public String getRejectionDescription() {
		return rejectionDescription;
	}

	public void setRejectionDescription(String rejectionDescription) {
		this.rejectionDescription = rejectionDescription;
	}
}
