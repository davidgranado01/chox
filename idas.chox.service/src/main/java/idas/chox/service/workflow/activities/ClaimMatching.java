package idas.chox.service.workflow.activities;

import java.util.Date;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingBand;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ClaimMatchingBandService;
import idas.chox.service.workflow.ClaimProcessWorkflowContext;

/**
 *
 * @author john
 */
public class ClaimMatching extends BaseActivity {
    static final Logger LOG = LoggerFactory.getLogger(ClaimMatching.class);
    private String claimNumber;
    private String indemnityStance;
    private BigDecimal liabilityInsurer;
    private String liabilityStance;
    private Date incidentDate;
    private String thirdPartyVehicleRegistration;
    private ClaimMatchingBandService claimMatchingBandService;
    private ClaimMatchingBand claimMatchingBand;

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (!claim.getInsurer().isEnableClaimMatching()) {
            LOG.error("Claim matching not enabled for insurer for claim '{}' ({})", claim.getChoReference(), claim.getId());
            throw new Exception("Claim matching not enabled for insurer");
        }
        
        if (!claim.getBreBand().isClaimMatchingEnable()) {
               LOG.error("Claim matching not enabled in BreBand for claim '{}' ({})", claim.getChoReference(), claim.getId());
            throw new Exception("Claim matching not enabled in BreBand");
        }

        claimMatchingBand = claimMatchingBandService.getClaimMatchingBand(
                    claim.getBreBand().getId(),
                    claim.getClaimType(), claim.getCustomer().getVehicleClass().getName());
        if (claimMatchingBand == null) {
            LOG.error("No claim matching bound found for claim '{}' ({})", claim.getChoReference(), claim.getId());
            throw new Exception("No Claim Matching Band Found");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        // Update Insurer Claim Number and Indemnity Stance
        claim.setMatchStatus(1);
        if (claim.getClaimNumber()==null || !claim.getClaimNumber().equals(claimNumber)) {
            claim.setClaimNumber(claimNumber);
        }
        if ((claim.getIndemnityStance() == null && indemnityStance != null) || !claim.getIndemnityStance().equals(indemnityStance)) {
            String stance = getIndemnityStanceFromString(indemnityStance.trim());
            if (stance != null) {
                claim.setIndemnityStance(stance);
                // Add Note
                claim.addComment(Comment.newComment(0, "Insurer Indemnity Stance: " + indemnityStance));
            }
        }
        
        if (liabilityInsurer != null && liabilityInsurer.compareTo(claimMatchingBand.getLiabilityPercentage()) >= 0) {
            // Full Claim Matched - set Liability
            
            try {
                LiabilityStatus liabilityStatus = getLiabilityStatusFromStance(liabilityStance.trim());
                if (liabilityStatus == LiabilityStatus.LIABILITY_ACCEPTED && liabilityInsurer.compareTo(new BigDecimal("100.00")) != 0) {
                    LOG.error("Invalid Liability % provided for claim matching for 'Full Liability Accepted': " + liabilityInsurer);
                    return;
                }
                if (liabilityStatus == LiabilityStatus.LIABILITY_REPUDIATED && liabilityInsurer.compareTo(BigDecimal.ZERO) != 0) {
                    LOG.error("Invalid Liability % provided for claim matching for 'Liability Repudiated': " + liabilityInsurer);
                    return;
                }
                claimService.setLiability(claim, liabilityStatus);
            } catch (IllegalArgumentException ex) {
                LOG.error("Invalid Liability Stance provided for claim matching: " + liabilityStance, ex);
                return;
            }
            claim.setLiabilityPercentages(liabilityInsurer, (new BigDecimal("100.00")).subtract(liabilityInsurer));
            claim.setMatchStatus(2);
            if (claimMatchingBand.isAutoAcknowledge()) {
                progressClaim(claim);
                claim.setMatchStatus(3);
            }
        }
    }

    private String getIndemnityStanceFromString(String stance) {
        switch(stance) {
                case "Dealing Under Article 75":
                case "Dealing Under Road Traffic Act":
                case "No Involvement":
                case "Not Indemnifying":
                case "Pending Indemnity":
                case "Providing Indemnity":
                    return stance;
                default:
                    return null;
        }
    }

    private LiabilityStatus getLiabilityStatusFromStance(String stance) {
        LiabilityStatus liabilityStatus;
        
        try {
            liabilityStatus = LiabilityStatus.getLiabilityStatus(stance);
        } catch (IllegalArgumentException ex) {
            switch (stance) {
                case "Liability Accepted":
                    liabilityStatus = LiabilityStatus.LIABILITY_ACCEPTED;
                    break;
                default:
                    throw ex;
            }
        }
        return liabilityStatus;
    }
   
    @Override
    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
        getDataService().save(claim);

        activityEventGenerator.getEvents(claim, this).stream().forEach((event) -> {
            ((ClaimProcessWorkflowContext)this.getWorkflowContext()).getEventBus().post(event);
        });
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public void setIndemnityStance(String indemnityStance) {
        this.indemnityStance = indemnityStance;
    }

    public void setLiabilityInsurer(BigDecimal liabilityInsurer) {
        this.liabilityInsurer = liabilityInsurer;
    }

    public void setLiabilityStance(String liabilityStance) {
        this.liabilityStance = liabilityStance;
    }

    public void setIncidentDate(Date incidentDate) {
        this.incidentDate = incidentDate;
    }

    public void setThirdPartyVehicleRegistration(String thirdPartyVehicleRegistration) {
        this.thirdPartyVehicleRegistration = thirdPartyVehicleRegistration;
    }

    public void setClaimMatchingBandService(ClaimMatchingBandService claimMatchingBandService) {
        this.claimMatchingBandService = claimMatchingBandService;
    }

    public void setClaimMatchingBand(ClaimMatchingBand claimMatchingBand) {
        this.claimMatchingBand = claimMatchingBand;
    }
    
    private void progressClaim(Claim claim) {
        switch (claim.getStatus()) {
            case ClaimStatus.CLAIM_REFERRED_TO_FNOL:
                claim.setStatus(claim.getPreviousStatus());
                logTransaction(claim);
                setCurrentStatus(claim.getStatus());
                progressClaim(claim);
                break;
            case ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED:
                claim.setWorkgroup(claim.getBreBand().getClaimMatchingWorkgroup());
                if (claim.getInsurer().isClaimOwnershipEnable()) {
                    claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
                    logTransaction(claim);
                    setCurrentStatus(claim.getStatus());
                    progressClaim(claim);
                } else {
                    claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                    logTransaction(claim);
                    setCurrentStatus(claim.getStatus());
                    progressClaim(claim);
                }
                break;
            case ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED:
                WebUser claimOwner = claim.getBreBand().getClaimMatchingOwner();
                claim.setClaimOwner(claimOwner);
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                if (claim.getInsurer().isWorkgroupEnable()) {
                    claim.setWorkgroup(claim.getBreBand().getClaimMatchingWorkgroup());
                }
                if (claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
                    Comment comment = Comment.newComment(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ").");
                    claim.addComment(comment);
                }
                logTransaction(claim);
                setCurrentStatus(claim.getStatus());
                progressClaim(claim);
                break;
            case ClaimStatus.CLAIM_UPDATE_BY_ENG:
            case ClaimStatus.CLAIM_REJECTION_CONTESTED:
            case ClaimStatus.CLAIM_PENDING:
            case ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED:
                claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
                logTransaction(claim);
                break;
            default:
                LOG.error("Invalid claim status for acknowledging claim matched claim '{}: {}", claim.getChoReference(), claim.getStatus());
                break;
        }
    }
}
