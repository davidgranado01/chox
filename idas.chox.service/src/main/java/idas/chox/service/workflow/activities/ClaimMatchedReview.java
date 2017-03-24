package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;

public class ClaimMatchedReview extends BaseActivity {
    private int matchedWorkgroupIdField = -1;
    private int matchedClaimOwnerIdField = -1;
    private BigDecimal reserveValue;
    private WebUser claimOwner;
    private Workgroup workgroup;
    private boolean workgroupsEnabled;
    private boolean ownershipEnabled;

    // <editor-fold defaultstate="collapsed" desc="Parameter Getters">
    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        if (claim.getMatchStatus() == 4) {
            throw new Exception("Claim match has already been reviewed");
        }
        
        if (claim.getMatchStatus() != 3) {
            throw new Exception("Claim was not matched and auto-acknowledged so cannot be reviewed");
        }
        
        workgroupsEnabled = claim.getInsurer().isWorkgroupEnable();
        ownershipEnabled = claim.getInsurer().isClaimOwnershipEnable();
        
        if (workgroupsEnabled && matchedWorkgroupIdField <= 0) {
            throw new Exception("No workgroup selected");
        } else if (workgroupsEnabled) {
            workgroup = (Workgroup) getDataService().get(Workgroup.class, matchedWorkgroupIdField);
            if (workgroup == null) {
                throw new Exception("Invalid workgroup");
            }
            // Check workgroup belongs to the Insurer
            if (workgroup.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("Workgroup does not belong to Insurer");
            }
        }

        if (ownershipEnabled && matchedClaimOwnerIdField <= 0) {
            throw new Exception("No Claim Owner selected");
        } else if (ownershipEnabled) {
            claimOwner = (WebUser) getDataService().get(WebUser.class, matchedClaimOwnerIdField);
            if (claimOwner == null) {
                throw new Exception("Invalid Claim Owner");
            }
            // Check user belongs to the Insurer
            if (claimOwner.getInsurer().getId().intValue() != claim.getInsurer().getId().intValue()) {
                throw new AccessDeniedException("The selected Claim Owner does not belong to the Insurer of the claim.");
            }
        }

    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        if (ownershipEnabled && !Objects.equals(claim.getClaimOwner().getId(), claimOwner.getId())) {
            claim.setClaimOwner(claimOwner);
            if (claimOwner.getTelephone() != null && claimOwner.getTelephone().length() > 0) {
                Comment comment = Comment.newComment(0, "Insurer Claims Handler is '" + claimOwner.getFullName() + "' (contact number: " + claimOwner.getTelephone() + ").");
                claim.addComment(comment);
            }
        }
        
        if (workgroupsEnabled && !Objects.equals(claim.getWorkgroup().getId(), workgroup.getId())) {
            claim.setWorkgroup(workgroup);
            // Check STP status for new workgroup
            if (claim.getInvoice() != null && claim.getBreBand().isPaymentTeamActive() && !workgroup.isStpExcluded()
                    && ((ClaimType.isGTA(claim.getClaimType()) && claim.getInsurer().isGtaPaymentsTeamEnable())
                     || (ClaimType.isTPI(claim.getClaimType()) && claim.getInsurer().isTpiPaymentsTeamEnable())
                     || (ClaimType.isSubscriber(claim.getClaimType()) && claim.getInsurer().isSubscriberPaymentsTeamEnable())
                     || (ClaimType.isInsurerUpload(claim.getClaimType()) && claim.getInsurer().isInsurerManualPaymentsTeamEnable())
                     || (ClaimType.isInsurerVsInsurer(claim.getClaimType()) && claim.getInsurer().isInsurerVsInsurerPaymentsTeamEnable())
                     || (ClaimType.isFixedFee(claim.getClaimType()) && claim.getInsurer().isFixedFeePaymentsTeamEnable())
                     || (ClaimType.isCollaborationProtocol(claim.getClaimType()) && claim.getInsurer().isCollaborationPaymentsTeamEnable()))) {
                claim.getInvoice().setPaymentTeam(true);
            } else if (claim.getInvoice() != null && claim.getInvoice().isPaymentTeam() && workgroup.isStpExcluded()) {
                claim.getInvoice().setPaymentTeam(false);
            }
        }
        claim.setMatchStatus(4);
        claim.setIndemnityAmount(reserveValue);
    }

    public int getMatchedWorkgroupIdField() {
        return matchedWorkgroupIdField;
    }

    public void setMatchedWorkgroupIdField(int matchedWorkgroupIdField) {
        this.matchedWorkgroupIdField = matchedWorkgroupIdField;
    }

    public int getMatchedClaimOwnerIdField() {
        return matchedClaimOwnerIdField;
    }

    public void setMatchedClaimOwnerIdField(int matchedClaimOwnerIdField) {
        this.matchedClaimOwnerIdField = matchedClaimOwnerIdField;
    }

    public BigDecimal getReserveValue() {
        return reserveValue;
    }

    public void setReserveValue(BigDecimal reserveValue) {
        this.reserveValue = reserveValue;
    }

}
